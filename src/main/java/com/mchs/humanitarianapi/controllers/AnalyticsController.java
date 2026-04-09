package com.mchs.humanitarianapi.controllers;

import com.mchs.humanitarianapi.services.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
@Tag(name = "Аналитика", description = "Дашборды. Доступно только для ролей MANAGER и ADMIN")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/general")
    @Operation(summary = "Общая сводка", description = "Менеджер видит сводку по своему региону, Админ - по всей стране")
    public Map<String, Object> getGeneral(@Parameter(hidden = true) Principal principal) {
        return analyticsService.getGeneralStats(principal.getName());
    }

    @GetMapping("/statuses")
    @Operation(summary = "Документы по статусам", description = "Статистика для круговой диаграммы")
    public Map<String, Long> getStatuses(@Parameter(hidden = true) Principal principal) {
        return analyticsService.getCalculationsByStatus(principal.getName());
    }

    @GetMapping("/resources")
    @Operation(summary = "Расход ресурсов", description = "Агрегация количества необходимых ресурсов")
    public Map<String, Object> getResources(@Parameter(hidden = true) Principal principal) {
        return analyticsService.getTotalResourcesNeeded(principal.getName());
    }
}