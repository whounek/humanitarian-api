package com.mchs.humanitarianapi.services;

import com.mchs.humanitarianapi.models.User;
import com.mchs.humanitarianapi.repositories.CalculationItemRepository;
import com.mchs.humanitarianapi.repositories.CalculationRepository;
import com.mchs.humanitarianapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final CalculationRepository calculationRepository;
    private final CalculationItemRepository calculationItemRepository;
    private final UserRepository userRepository;

    public AnalyticsService(CalculationRepository calculationRepository,
                            CalculationItemRepository calculationItemRepository,
                            UserRepository userRepository) {
        this.calculationRepository = calculationRepository;
        this.calculationItemRepository = calculationItemRepository;
        this.userRepository = userRepository;
    }

    // Вспомогательный метод: проверка прав доступа
    private User getAuthorizedUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if ("OPERATOR".equals(user.getRole())) {
            throw new RuntimeException("ОШИБКА ДОСТУПА: Операторам запрещен просмотр аналитики!");
        }
        return user;
    }

    // 1. Общая статистика системы
    public Map<String, Object> getGeneralStats(String username) {
        User user = getAuthorizedUser(username);
        Map<String, Object> stats = new HashMap<>();

        if ("ADMIN".equals(user.getRole())) {
            // Глобальная статистика для суперадмина
            stats.put("total_calculations_created", calculationRepository.count());
            stats.put("total_users_registered", userRepository.count());
        } else {
            // Локальная статистика для менеджера (по его региону)
            stats.put("total_calculations_created", calculationRepository.countByAuthorRegion(user.getRegion()));
            stats.put("total_users_registered", userRepository.countByRegion(user.getRegion()));
            stats.put("region", user.getRegion()); // Добавляем маркер региона для удобства фронтенда
        }
        return stats;
    }

    // 2. Статистика расчетов по статусам
    public Map<String, Long> getCalculationsByStatus(String username) {
        User user = getAuthorizedUser(username);
        List<Object[]> results;

        if ("ADMIN".equals(user.getRole())) {
            results = calculationRepository.countCalculationsByStatus();
        } else {
            results = calculationRepository.countCalculationsByStatusForRegion(user.getRegion());
        }

        Map<String, Long> statusMap = new HashMap<>();
        for (Object[] row : results) {
            String statusName = (String) row[0];
            Long count = (Long) row[1];
            statusMap.put(statusName, count);
        }
        return statusMap;
    }

    // 3. Объем запрошенных ресурсов
    public Map<String, Object> getTotalResourcesNeeded(String username) {
        User user = getAuthorizedUser(username);
        List<Object[]> results;

        if ("ADMIN".equals(user.getRole())) {
            results = calculationItemRepository.sumTotalResourcesNeeded();
        } else {
            results = calculationItemRepository.sumTotalResourcesNeededByRegion(user.getRegion());
        }

        Map<String, Object> resourceMap = new HashMap<>();
        for (Object[] row : results) {
            String resourceName = (String) row[0];
            Object totalQuantity = row[1];
            resourceMap.put(resourceName, totalQuantity);
        }
        return resourceMap;
    }
}