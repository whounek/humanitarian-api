package com.mchs.humanitarianapi.services;

import com.mchs.humanitarianapi.models.Calculation;
import com.mchs.humanitarianapi.models.Status;
import com.mchs.humanitarianapi.models.User;
import com.mchs.humanitarianapi.repositories.CalculationRepository;
import com.mchs.humanitarianapi.repositories.StatusRepository;
import com.mchs.humanitarianapi.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkflowService {

    private final CalculationRepository calculationRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final ActionLogService actionLogService;

    public WorkflowService(CalculationRepository calculationRepository,
                           StatusRepository statusRepository,
                           UserRepository userRepository,
                           ActionLogService actionLogService) {
        this.calculationRepository = calculationRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.actionLogService = actionLogService;
    }

    // ШАГ 1: Отправка на проверку
    @Transactional
    public String sendToReview(Long calculationId, String username) {
        Calculation calc = calculationRepository.findById(calculationId)
                .orElseThrow(() -> new RuntimeException("Расчет не найден"));

        if ("На согласовании".equals(calc.getStatus().getName()) || "В работе".equals(calc.getStatus().getName())) {
            throw new RuntimeException("ОШИБКА: Документ уже отправлен или находится в работе!");
        }

        Status status = statusRepository.findByName("На согласовании")
                .orElseThrow(() -> new RuntimeException("Статус не найден в БД"));

        calc.setStatus(status);
        calculationRepository.save(calc);

        actionLogService.logAction(username, "Отправил расчет #" + calculationId + " на согласование");

        return "Документ #" + calculationId + " успешно отправлен руководителю.";
    }

    // ШАГ 2: Проверка руководителем
    @Transactional
    public String reviewDocument(Long calculationId, String username, String decision, String comment) {
        User reviewer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!"MANAGER".equals(reviewer.getRole()) && !"ADMIN".equals(reviewer.getRole())) {
            throw new RuntimeException("ОШИБКА ДОСТУПА: Только MANAGER или ADMIN могут утверждать документы!");
        }

        Calculation calc = calculationRepository.findById(calculationId)
                .orElseThrow(() -> new RuntimeException("Расчет не найден"));

        Status status = statusRepository.findByName(decision)
                .orElseThrow(() -> new RuntimeException("Статус '" + decision + "' не найден"));

        calc.setStatus(status);
        calc.setManagerComment(comment);
        calculationRepository.save(calc);

        actionLogService.logAction(username, "Установил статус '" + decision + "' для расчета #" + calculationId);
        return "Решение сохранено.";
    }

    // ШАГ 3: Выпуск в работу (ТОТ САМЫЙ ПРОПАВШИЙ МЕТОД)
    @Transactional
    public String finalizeDocument(Long calculationId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!"MANAGER".equals(user.getRole()) && !"ADMIN".equals(user.getRole())) {
            throw new RuntimeException("ОШИБКА: Выпускать документ в работу может только MANAGER или ADMIN!");
        }

        Calculation calc = calculationRepository.findById(calculationId)
                .orElseThrow(() -> new RuntimeException("Расчет не найден"));

        if (!"Согласовано".equals(calc.getStatus().getName())) {
            throw new RuntimeException("ОШИБКА: Нельзя финализировать документ без статуса 'Согласовано'");
        }

        Status status = statusRepository.findByName("В работе")
                .orElseThrow(() -> new RuntimeException("Статус 'В работе' не найден"));

        calc.setStatus(status);
        calculationRepository.save(calc);

        actionLogService.logAction(username, "Выпустил расчет #" + calculationId + " в работу");

        return "Документ #" + calculationId + " переведен в финальный статус 'В работе'.";
    }

    // Получение расчетов для менеджера по региону
    public List<Calculation> getCalculationsForManager(String username) {
        User manager = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if ("ADMIN".equals(manager.getRole())) {
            return calculationRepository.findAll();
        }

        return calculationRepository.findAllByAuthorRegion(manager.getRegion());
    }

    // Генерация PDF
    public byte[] generatePdfReport(Long calculationId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if ("OPERATOR".equals(user.getRole())) {
            throw new RuntimeException("ОШИБКА ДОСТУПА: Выгрузка PDF-отчетов доступна только координаторам!");
        }

        actionLogService.logAction(username, "Выгрузил PDF для расчета #" + calculationId);
        return new byte[0];
    }
}