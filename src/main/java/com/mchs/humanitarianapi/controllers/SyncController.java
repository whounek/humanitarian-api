package com.mchs.humanitarianapi.controllers;

import com.mchs.humanitarianapi.services.SyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/sync")
@Tag(name = "Синхронизация", description = "Автоматическое и ручное обновление нормативов из внешних источников")
public class SyncController {

    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }

    @PostMapping("/standards")
    @Operation(summary = "Синхронизировать нормы", description = "Только для ADMIN. Принудительный запуск обновления нормативов.")
    public String sync(@Parameter(hidden = true) Principal principal) {
        return syncService.manualSync(principal.getName());
    }
}