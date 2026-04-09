package com.mchs.humanitarianapi.controllers;

import com.mchs.humanitarianapi.models.ApprovalHistory;
import com.mchs.humanitarianapi.models.ActionLog;
import com.mchs.humanitarianapi.services.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@Tag(name = "Системный аудит", description = "Сквозное логирование действий (Только для ADMIN)")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/logs")
    @Operation(summary = "Логи действий", description = "Извлечение данных об изменениях в системе")
    public List<ActionLog> getActivityLog(@Parameter(hidden = true) Principal principal) {
        // Передаем логин текущего юзера для проверки прав
        return auditService.getActivityLogs(principal.getName());
    }

    @GetMapping("/approvals")
    @Operation(summary = "Журнал согласования", description = "История решений и комментарии руководства")
    public List<ApprovalHistory> getApprovalHistory(
            @Parameter(description = "ID документа (необязательно, если нужен весь список)")
            @RequestParam(required = false) Long calculationId,
            @Parameter(hidden = true) Principal principal) {
        return auditService.getApprovalHistory(calculationId, principal.getName());
    }
}