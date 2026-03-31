package com.mchs.humanitarianapi.controllers;

import com.mchs.humanitarianapi.models.DisasterType;
import com.mchs.humanitarianapi.models.Resource;
import com.mchs.humanitarianapi.services.DictionaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dictionaries")
@Tag(name = "Справочная информация", description = "Предоставление готовой информации из справочников БД")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    // Вот этот явный конструктор скажет Spring Boot точно внедрить сервис!
    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping("/disasters")
    @Operation(summary = "Получить типы ЧС", description = "Тянет список видов ЧС напрямую из базы данных")
    public List<DisasterType> getDisasterTypes() {
        return dictionaryService.getAllDisasterTypes(); // Теперь тут не будет null!
    }

    @GetMapping("/resources")
    @Operation(summary = "Получить список ресурсов", description = "Тянет каталог ресурсов из базы данных")
    public List<Resource> getResources() {
        return dictionaryService.getAllResources();
    }
}