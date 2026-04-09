package com.mchs.humanitarianapi.controllers;

import com.mchs.humanitarianapi.models.User;
import com.mchs.humanitarianapi.security.JwtUtil;
import com.mchs.humanitarianapi.services.UserService;
import com.mchs.humanitarianapi.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Авторизация", description = "Регистрация и вход (JWT)")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthController(UserService userService, JwtUtil jwtUtil, UserRepository userRepository) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация нового сотрудника")
    public User register(
            @Parameter(description = "Логин") @RequestParam String username,
            @Parameter(description = "Пароль") @RequestParam String password,
            @Parameter(description = "ФИО") @RequestParam String fullName) {

        return userService.registerUser(username.trim(), password.trim(), fullName.trim());
    }

    @PostMapping("/login")
    @Operation(summary = "Вход в систему", description = "Возвращает JWT-токен. Скопируйте его без кавычек и вставьте в кнопку Authorize")
    public String login(
            @Parameter(description = "Логин") @RequestParam String username,
            @Parameter(description = "Пароль") @RequestParam String password) {

        // 1. Проверяем, правильный ли пароль
        userService.login(username.trim(), password.trim());

        // 2. Достаем пользователя из базы данных (распаковываем Optional)
        User user = userRepository.findByUsername(username.trim())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден в базе"));

        // 3. Защита на случай, если роли в базе нет
        String role = (user.getRole() != null) ? user.getRole() : "USER";

        // 4. Генерируем токен с ДВУМЯ параметрами (логин и роль)
        return jwtUtil.generateToken(user.getUsername(), role);
    }
}