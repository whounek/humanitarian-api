package com.mchs.humanitarianapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@Tag(name = "Системный аудит", description = "Сквозное логирование действий всех участников системы")
public class AuditController {

    @GetMapping("/logs")
    @Operation(summary = "Логи действий", description = "Извлечение данных об изменениях в расчетах и справочниках")
    public List<String> getActivityLog() {
        return List.of("10:00 - Создан черновик", "10:15 - Обновлен справочник");
    }

    @GetMapping("/approvals")
    @Operation(summary = "Журнал согласования", description = "История решений и комментарии руководства")
    public List<String> getApprovalHistory() {
        return List.of("Документ #1: Отклонен (Нет данных по воде)", "Документ #1: Утвержден");
    }
}