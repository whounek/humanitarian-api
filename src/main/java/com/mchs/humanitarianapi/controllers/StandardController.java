package com.mchs.humanitarianapi.controllers;

import com.mchs.humanitarianapi.models.Standard;
import com.mchs.humanitarianapi.repositories.StandardRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/standards")
@Tag(name = "Нормативы", description = "Справочник норм потребления")
public class StandardController {

    private final StandardRepository standardRepository;

    public StandardController(StandardRepository standardRepository) {
        this.standardRepository = standardRepository;
    }

    @GetMapping("/")
    @Operation(summary = "Получить все актуальные нормативы", description = "Выгружает нормы напрямую из базы данных")
    public List<Standard> getAllStandards() {

        return standardRepository.findAll();
    }
}