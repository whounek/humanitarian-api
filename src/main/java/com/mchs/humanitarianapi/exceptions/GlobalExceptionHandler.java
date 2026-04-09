package com.mchs.humanitarianapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException; // Важный импорт для 403 ошибки
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. ПЕРЕХВАТЧИК ОШИБОК БЕЗОПАСНОСТИ (403 Forbidden)
    // Срабатывает, когда Оператор лезет туда, куда можно только Админу
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        body.put("status", HttpStatus.FORBIDDEN.value()); // 403
        body.put("error", "Доступ запрещен");
        body.put("message", "У вас нет прав для выполнения этой операции. Обратитесь к руководству.");

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    // 2. Перехватчик наших бизнес-ошибок (400 Bad Request)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        body.put("status", HttpStatus.BAD_REQUEST.value()); // 400
        body.put("error", "Ошибка бизнес-логики");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // 3. Перехватчик всех остальных системных ошибок (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()); // 500
        body.put("error", "Системный сбой");
        body.put("message", "Произошла непредвиденная ошибка на сервере. Обратитесь к администратору.");

        System.err.println("[СИСТЕМНАЯ ОШИБКА]: " + ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}