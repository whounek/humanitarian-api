package com.mchs.humanitarianapi.services;

import com.mchs.humanitarianapi.models.ActionLog;
import com.mchs.humanitarianapi.models.ApprovalHistory;
import com.mchs.humanitarianapi.models.User;
import com.mchs.humanitarianapi.repositories.ActionLogRepository;
import com.mchs.humanitarianapi.repositories.ApprovalHistoryRepository;
import com.mchs.humanitarianapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    private final ActionLogRepository actionLogRepository; // Заменили AuditLog на ActionLog
    private final ApprovalHistoryRepository approvalHistoryRepository;
    private final UserRepository userRepository;

    public AuditService(ActionLogRepository actionLogRepository,
                        ApprovalHistoryRepository approvalHistoryRepository,
                        UserRepository userRepository) {
        this.actionLogRepository = actionLogRepository;
        this.approvalHistoryRepository = approvalHistoryRepository;
        this.userRepository = userRepository;
    }

    // Вспомогательный метод: проверка прав
    private void checkAdminAccess(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!"ADMIN".equals(user.getRole())) {
            throw new RuntimeException("ОТКАЗАНО: Просмотр системного аудита доступен только СУПЕРАДМИНУ!");
        }
    }

    public List<ActionLog> getActivityLogs(String username) {
        checkAdminAccess(username); // Проверяем права!
        return actionLogRepository.findAll(); // Отдаем реальные логи
    }

    public List<ApprovalHistory> getApprovalHistory(Long calculationId, String username) {
        checkAdminAccess(username); // Проверяем права!

        if (calculationId != null) {
            return approvalHistoryRepository.findByCalculationId(calculationId);
        }
        return approvalHistoryRepository.findAll();
    }
}