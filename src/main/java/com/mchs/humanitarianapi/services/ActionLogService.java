package com.mchs.humanitarianapi.services;

import com.mchs.humanitarianapi.models.ActionLog;
import com.mchs.humanitarianapi.repositories.ActionLogRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActionLogService {

    private final ActionLogRepository logRepository;

    public ActionLogService(ActionLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    // Метод для ЗАПИСИ лога (мы будем вызывать его из WorkflowService)
    public void logAction(String username, String actionDetail) {
        ActionLog log = new ActionLog();
        log.setUsername(username);
        log.setActionDetail(actionDetail);
        log.setTimestamp(LocalDateTime.now());
        logRepository.save(log); // Вот тут лог физически падает в БД!
    }

    // Метод для ЧТЕНИЯ логов (Только для Админа/Менеджера)
    public List<ActionLog> getAllLogs() {
        return logRepository.findAll();
    }
}