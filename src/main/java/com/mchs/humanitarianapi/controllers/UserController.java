package com.mchs.humanitarianapi.controllers;

import com.mchs.humanitarianapi.models.User;
import com.mchs.humanitarianapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Сотрудники (Админка)", description = "Управление учетными записями МЧС")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Получить список всех сотрудников")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}/block")
    @Operation(summary = "Блокировка сотрудника (Soft Delete)", description = "Отстраняет сотрудника от работы, не удаляя его историю из БД")
    public User blockUser(@Parameter(description = "ID сотрудника") @PathVariable Long id) {
        return userService.blockUser(id);
    }
}