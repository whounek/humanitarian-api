package com.mchs.humanitarianapi.controllers;

import com.mchs.humanitarianapi.models.User;
import com.mchs.humanitarianapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize; // Импортируем броню
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@Tag(name = "Админка", description = "Управление персоналом (Только для ADMIN)")
@PreAuthorize("hasRole('ADMIN')") // Вешаем жесткий замок на весь контроллер
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Получить список всех сотрудников")
    public List<User> getAllUsers(@Parameter(hidden = true) Principal principal) {
        // Передаем логин текущего пользователя для проверки прав
        return userService.getAllUsers(principal.getName());
    }

    @PutMapping("/{id}/block")
    @Operation(summary = "Заблокировать сотрудника", description = "Отстраняет сотрудника от работы (Soft Delete)")
    public String blockUser(@PathVariable Long id, @Parameter(hidden = true) Principal principal) {
        return userService.blockUser(id, principal.getName());
    }

    @PutMapping("/{id}/unblock")
    @Operation(summary = "Разблокировать сотрудника", description = "Восстанавливает доступ к системе")
    public String unblockUser(@PathVariable Long id, @Parameter(hidden = true) Principal principal) {
        return userService.unblockUser(id, principal.getName());
    }
}