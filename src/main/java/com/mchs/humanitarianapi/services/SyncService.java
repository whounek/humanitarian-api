package com.mchs.humanitarianapi.services;

import com.mchs.humanitarianapi.models.User;
import com.mchs.humanitarianapi.repositories.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SyncService {

    private final StandardSyncService standardSyncService;
    private final ActionLogService actionLogService;
    private final UserRepository userRepository;

    public SyncService(StandardSyncService standardSyncService,
                       ActionLogService actionLogService,
                       UserRepository userRepository) {
        this.standardSyncService = standardSyncService;
        this.actionLogService = actionLogService;
        this.userRepository = userRepository;
    }

    // 1. АВТОМАТИЧЕСКАЯ СИНХРОНИЗАЦИЯ (раз в 30 минут)
    @Scheduled(fixedRate = 1800000)
    public void autoSync() {
        System.out.println("[СИСТЕМА] Запуск фонового обновления нормативов МЧС...");
        try {
            String result = standardSyncService.syncWithGlobalStandards();
            actionLogService.logAction("SYSTEM", "Автоматическая синхронизация: " + result);
        } catch (Exception e) {
            System.err.println("[ОШИБКА АВТО-СИНХРОНИЗАЦИИ]: " + e.getMessage());
            actionLogService.logAction("SYSTEM", "Ошибка авто-синхронизации: " + e.getMessage());
        }
    }

    // 2. РУЧНАЯ СИНХРОНИЗАЦИЯ (по кнопке)
    public String manualSync(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!"ADMIN".equals(user.getRole())) {
            throw new RuntimeException("ОТКАЗАНО: Принудительный запуск обновления доступен только СУПЕРАДМИНУ!");
        }

        try {
            String result = standardSyncService.syncWithGlobalStandards();
            actionLogService.logAction(username, "Ручная синхронизация: " + result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при ручной синхронизации: " + e.getMessage());
        }
    }
}