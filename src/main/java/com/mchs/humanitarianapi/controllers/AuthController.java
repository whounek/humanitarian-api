package com.mchs.humanitarianapi.controllers;

import com.mchs.humanitarianapi.models.User;
import com.mchs.humanitarianapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Авторизация", description = "Регистрация и вход сотрудников в систему")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация нового сотрудника")
    public User register(
            @Parameter(description = "Логин") @RequestParam String username,
            @Parameter(description = "Пароль") @RequestParam String password,
            @Parameter(description = "ФИО") @RequestParam String fullName) {
        return userService.registerUser(username, password, fullName);
    }

    @PostMapping("/login")
    @Operation(summary = "Вход в систему (Получение токена)")
    public String login(
            @Parameter(description = "Логин") @RequestParam String username,
            @Parameter(description = "Пароль") @RequestParam String password) {
        return userService.login(username, password);
    }
}