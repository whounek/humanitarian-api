package com.mchs.humanitarianapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/standards")
@Tag(name = "Нормативы и стандарты", description = "База норм выдачи ресурсов на 1 человека")
public class StandardController {

    @GetMapping("/")
    @Operation(summary = "Получить список норм", description = "Связь: Тип ЧС — Ресурс — Норма на чел.")
    public List<String> getStandards() {
        return List.of("Вода (Лето): 10л/чел", "Питание: 2400 ккал/чел");
    }

    @PutMapping("/update")
    @Operation(summary = "Обновить норматив", description = "Изменение нормы потребления")
    public String updateStandard() {
        return "Норматив успешно обновлен";
    }

    @PostMapping("/calculate-base")
    @Operation(summary = "Базовый расчет", description = "Расчет базовой потребности без коэффициентов")
    public String calculateBase() {
        return "Базовый расчет выполнен";
    }
}