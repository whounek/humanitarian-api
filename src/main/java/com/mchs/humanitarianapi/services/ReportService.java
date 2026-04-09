package com.mchs.humanitarianapi.services;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.mchs.humanitarianapi.models.Calculation;
import com.mchs.humanitarianapi.models.CalculationItem;
import com.mchs.humanitarianapi.models.User;
import com.mchs.humanitarianapi.repositories.CalculationRepository;
import com.mchs.humanitarianapi.repositories.CalculationItemRepository;
import com.mchs.humanitarianapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReportService {

    private final CalculationRepository calculationRepository;
    private final CalculationItemRepository calculationItemRepository;
    private final UserRepository userRepository; // Для проверки ролей
    private final ActionLogService actionLogService; // Для записи логов

    private static final String FONT_PATH = "src/main/resources/fonts/Gilroy.ttf";

    public ReportService(CalculationRepository calculationRepository,
                         CalculationItemRepository calculationItemRepository,
                         UserRepository userRepository,
                         ActionLogService actionLogService) {
        this.calculationRepository = calculationRepository;
        this.calculationItemRepository = calculationItemRepository;
        this.userRepository = userRepository;
        this.actionLogService = actionLogService;
    }

    public byte[] generateCalculationPdf(Long calculationId, String username) {
        // 1. Проверяем, кто пытается скачать документ
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if ("OPERATOR".equals(user.getRole())) {
            throw new RuntimeException("ОШИБКА ДОСТУПА: Операторам запрещена выгрузка PDF-отчетов!");
        }

        // 2. Достаем данные расчета
        Calculation calculation = calculationRepository.findById(calculationId)
                .orElseThrow(() -> new RuntimeException("Расчет не найден"));

        List<CalculationItem> items = calculationItemRepository.findByCalculationId(calculationId);

        // 3. Записываем действие в лог
        actionLogService.logAction(username, "Выгрузил официальный PDF-отчет для расчета #" + calculationId);

        // 4. Генерация PDF (твой код, он отличный)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont russianFont = PdfFontFactory.createFont(FONT_PATH, PdfEncodings.IDENTITY_H);
            document.setFont(russianFont);

            document.add(new Paragraph("ОТЧЕТ ПО РАСЧЕТУ ГУМАНИТАРНОЙ ПОМОЩИ").setBold().setFontSize(16));
            document.add(new Paragraph("Номер документа: #" + calculation.getId()));
            document.add(new Paragraph("Тип ЧС: " + calculation.getDisasterType().getName()));
            document.add(new Paragraph("Количество пострадавших: " + calculation.getAffectedPeople()));
            document.add(new Paragraph("Статус: " + calculation.getStatus().getName()));
            document.add(new Paragraph("Дата создания: " + calculation.getCreatedAt()));

            document.add(new Paragraph("\nСПЕЦИФИКАЦИЯ РЕСУРСОВ:").setBold());

            Table table = new Table(UnitValue.createPointArray(new float[]{200f, 100f, 100f}));
            table.addHeaderCell(new Paragraph("Ресурс").setBold());
            table.addHeaderCell(new Paragraph("Количество").setBold());
            table.addHeaderCell(new Paragraph("Автор").setBold());

            for (CalculationItem item : items) {
                table.addCell(item.getResource().getName());
                table.addCell(item.getQuantity().toString());
                table.addCell(calculation.getAuthor().getFullName());
            }

            document.add(table);
            document.add(new Paragraph("\n\nДокумент сформирован автоматически системой Disaster Relief API."));

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при генерации PDF: " + e.getMessage());
        }

        return baos.toByteArray();
    }
}