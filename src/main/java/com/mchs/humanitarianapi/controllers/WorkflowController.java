package com.mchs.humanitarianapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workflow")
@Tag(name = "Документооборот", description = "Передача расчетов по цепочке «исполнитель — контролер»")
public class WorkflowController {

    @PostMapping("/{id}/send-to-review")
    @Operation(summary = "Отправить на проверку", description = "Младший сотрудник передает данные начальству")
    public String sendToReview(@PathVariable int id) {
        return "Документ " + id + " отправлен на согласование.";
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Утвердить документ", description = "Начальник проверяет и согласовывает документ")
    public String approve(@PathVariable int id) {
        return "Документ " + id + " успешно утвержден.";
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Отклонить с комментарием", description = "Возврат документа нижестоящему с указанием причины")
    public String rejectWithComment(@PathVariable int id, @RequestParam String comment) {
        return "Документ " + id + " отклонен. Причина: " + comment;
    }
}