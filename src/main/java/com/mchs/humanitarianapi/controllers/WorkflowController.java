package com.mchs.humanitarianapi.controllers;

import com.mchs.humanitarianapi.services.WorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/workflow")
@Tag(name = "Документооборот", description = "Управление жизненным циклом расчетов (JWT защита)")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping("/{id}/1-send-to-review")
    @Operation(summary = "Шаг 1. Отправить на проверку", description = "Оператор отправляет черновик. Доступно всем авторизованным сотрудникам.")
    public String sendToReview(
            @PathVariable Long id,
            @Parameter(hidden = true) Principal principal) { // Добавлен Principal

        // Теперь мы передаем и ID, и логин пользователя (для логов)
        return workflowService.sendToReview(id, principal.getName());
    }

    @PostMapping("/{id}/2-review")
    @Operation(summary = "Шаг 2. Проверка руководителем", description = "Только для MANAGER или ADMIN. Логин извлекается из JWT токена.")
    public String reviewDocument(
            @PathVariable Long id,
            @Parameter(hidden = true) Principal principal,
            @RequestParam @Parameter(description = "Введите: Согласовано или Отклонено") String decisionStatus,
            @RequestParam @Parameter(description = "Комментарий руководителя") String comment) {

        return workflowService.reviewDocument(id, principal.getName(), decisionStatus, comment);
    }

    @PostMapping("/{id}/3-finalize")
    @Operation(summary = "Шаг 3. Выпустить в работу", description = "Только для MANAGER или ADMIN. Финализация документа.")
    public String finalizeDocument(
            @PathVariable Long id,
            @Parameter(hidden = true) Principal principal) { // Добавлен Principal

        // Передаем логин для проверки прав и записи в лог
        return workflowService.finalizeDocument(id, principal.getName());
    }
}