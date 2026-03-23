package com.mchs.humanitarianapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Управление доступом", description = "Авторизация и распределение ролей сотрудников")
public class AuthController {

    @PostMapping("/login")
    @Operation(summary = "Авторизация", description = "Возвращает JWT-токен и данные пользователя (id, role)")
    public String login() {
        return "{ \"token\": \"eyJhbG...\", \"role\": \"USER\" }";
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация", description = "Регистрация нового сотрудника в системе")
    public String register() {
        return "Пользователь успешно зарегистрирован";
    }

    @PostMapping("/refresh")
    @Operation(summary = "Обновление токена", description = "Обновляет сессию пользователя")
    public String refreshToken() {
        return "{ \"token\": \"new_eyJhbG...\" }";
    }
}