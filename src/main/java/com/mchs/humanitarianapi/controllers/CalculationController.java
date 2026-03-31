package com.mchs.humanitarianapi.controllers;

import com.mchs.humanitarianapi.services.CalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/calculations")
@Tag(name = "Расчеты", description = "Управление расчетами гуманитарной помощи")
public class CalculationController {

    private final CalculationService calculationService;

    public CalculationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @GetMapping("/preview")
    @Operation(summary = "Полный расчет по ГОСТ (Ресурсы + Логистика)",
            description = "Считает объем ресурсов (Q = N * q * T * K) и количество техники (M = Q / L)")
    public Map<String, Object> previewCalculation(
            @Parameter(description = "ID типа ЧС (1 - Наводнение)") @RequestParam Long disasterTypeId,
            @Parameter(description = "N: Количество пострадавших") @RequestParam Integer affectedPeople,
            @Parameter(description = "T: Период (суток)") @RequestParam Integer days,
            @Parameter(description = "K: Климатический коэффициент") @RequestParam(required = false, defaultValue = "1.0") BigDecimal climateCoefficient,
            @Parameter(description = "L: Вместимость грузовика (например, 1500 для Газели)") @RequestParam(required = false, defaultValue = "1500") BigDecimal truckCapacity) {

        return calculationService.previewCalculation(disasterTypeId, affectedPeople, days, climateCoefficient, truckCapacity);
    }
}