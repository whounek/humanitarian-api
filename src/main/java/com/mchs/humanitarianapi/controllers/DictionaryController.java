package com.mchs.humanitarianapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dictionaries")
@Tag(name = "Справочная информация", description = "Предоставление готовой информации из справочников")
public class DictionaryController {

    @GetMapping("/disasters")
    @Operation(summary = "Получить типы ЧС", description = "Справочник видов чрезвычайных ситуаций")
    public List<String> getDisasterTypes() {
        return List.of("Наводнение", "Землетрясение", "Техногенная катастрофа");
    }

    @GetMapping("/resources")
    @Operation(summary = "Получить список ресурсов", description = "Каталог ресурсов по категориям")
    public List<String> getResources() {
        return List.of("Питьевая вода", "Консервы", "Теплые одеяла", "Медикаменты");
    }

    @GetMapping("/shelters")
    @Operation(summary = "Получить список ПВР", description = "Справочник пунктов временного размещения")
    public List<String> getShelters() {
        return List.of("ПВР №1 (Школа 12)", "ПВР №2 (Спорткомплекс)");
    }
}