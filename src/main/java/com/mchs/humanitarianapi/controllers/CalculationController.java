package com.mchs.humanitarianapi.controllers;

import com.mchs.humanitarianapi.models.Calculation;
import com.mchs.humanitarianapi.models.Shelter;
import com.mchs.humanitarianapi.repositories.CalculationRepository;
import com.mchs.humanitarianapi.services.CalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/calculations")
@Tag(name = "Расчеты", description = "Управление документами расчетов")
public class CalculationController {

    private final CalculationService calculationService;
    private final CalculationRepository calculationRepository;

    public CalculationController(CalculationService calculationService,
                                 CalculationRepository calculationRepository) {
        this.calculationService = calculationService;
        this.calculationRepository = calculationRepository;
    }

    // --- 1. ПРЕДВАРИТЕЛЬНЫЙ РАСЧЕТ
    @GetMapping("/preview")
    @Operation(summary = "Предварительный расчет", description = "Быстрый расчет ресурсов по формулам без сохранения в базу")
    public Map<String, Object> previewCalculation(
            @RequestParam Long disasterTypeId,
            @RequestParam Integer affectedPeople,
            @RequestParam Integer days,
            @RequestParam(required = false) BigDecimal climateCoefficient,
            @RequestParam(required = false) BigDecimal truckCapacity) {

        return calculationService.previewCalculation(disasterTypeId, affectedPeople, days, climateCoefficient, truckCapacity);
    }

    // --- 2. СОЗДАНИЕ ЧЕРНОВИКА В БАЗЕ ---
    @PostMapping("/draft")
    @Operation(summary = "Создать черновик", description = "Создает новый расчет ресурсов с привязкой к автору")
    public Calculation createDraft(@RequestParam Long disasterTypeId,
                                   @RequestParam Integer affectedPeople,
                                   @RequestParam Long authorId) {
        return calculationService.createDraft(disasterTypeId, affectedPeople, authorId);
    }

    // --- 3. ИНТЕЛЛЕКТУАЛЬНЫЙ ПОДБОР ПВР ---
    @GetMapping("/{id}/suggest-shelters")
    @Operation(summary = "Интеллектуальный подбор ПВР", description = "Автоматически подбирает комбинацию ПВР в регионе ЧС")
    public List<Shelter> suggestSheltersForCalculation(@PathVariable Long id) {
        // Мы передаем в сервис ID расчета, а он сам достанет регион и людей
        return calculationService.suggestShelters(id);
    }
}