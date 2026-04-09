package com.mchs.humanitarianapi.controllers;

import com.mchs.humanitarianapi.services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "Отчеты", description = "Генерация официальных документов в формате PDF")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = "/{calculationId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(summary = "Скачать PDF-отчет", description = "Доступно только MANAGER и ADMIN. Генерирует PDF-файл с результатами расчета")
    public ResponseEntity<byte[]> getReportPdf(
            @PathVariable Long calculationId,
            @Parameter(hidden = true) Principal principal) { // Достаем текущего пользователя из токена

        // Передаем логин в сервис
        byte[] pdfContent = reportService.generateCalculationPdf(calculationId, principal.getName());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_" + calculationId + ".pdf")
                .body(pdfContent);
    }
}