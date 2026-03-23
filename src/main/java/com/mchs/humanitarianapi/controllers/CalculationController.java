package com.mchs.humanitarianapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/calculations")
@Tag(name = "Расчет ресурсов", description = "Вычисление необходимого объема гуманитарной помощи")
public class CalculationController {

    @PostMapping("/draft")
    @Operation(summary = "Создать черновик", description = "Инициализация нового расчета")
    public String createDraft() {
        return "{ \"calculation_id\": 1, \"status\": \"DRAFT\" }";
    }

    @PostMapping("/total")
    @Operation(summary = "Рассчитать итог", description = "Умножение количества пострадавших на нормы из справочника")
    public String calculateTotal() {
        return "Полный расчет завершен. Транспорт: 3 Газели.";
    }

    @GetMapping("/history")
    @Operation(summary = "История расчетов", description = "Получение списка прошлых калькуляций")
    public List<String> getHistory() {
        return List.of("Расчет #1 (Завершен)", "Расчет #2 (В работе)");
    }
}