package com.mchs.humanitarianapi.services;

import com.mchs.humanitarianapi.models.User;
import com.mchs.humanitarianapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ActionLogService actionLogService; // Подключаем логи!

    public UserService(UserRepository userRepository, ActionLogService actionLogService) {
        this.userRepository = userRepository;
        this.actionLogService = actionLogService;
    }

    public User registerUser(String username, String password, String fullName) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // В реальном проекте пароль нужно шифровать
        user.setFullName(fullName);
        user.setIsActive(true);
        return userRepository.save(user);
    }

    // ИСПРАВЛЕНА КРИТИЧЕСКАЯ УЯЗВИМОСТЬ (теперь метод выбрасывает ошибку при неверном вводе)
    public void login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Ошибка: Пользователь не найден"));

        if (!user.getIsActive()) {
            throw new RuntimeException("Ошибка: Аккаунт сотрудника заблокирован (уволен/отстранен)!");
        }

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Ошибка: Неверный пароль");
        }
    }

    // --- БЛОК АДМИНИСТРАТОРА ---

    // Вспомогательный метод для жесткой проверки прав
    private void checkAdminAccess(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!"ADMIN".equals(user.getRole())) {
            throw new RuntimeException("ОТКАЗАНО: Доступ разрешен только СУПЕРАДМИНУ!");
        }
    }

    public List<User> getAllUsers(String adminUsername) {
        checkAdminAccess(adminUsername); // Проверяем, что запрашивает админ
        return userRepository.findAll();
    }

    public String blockUser(Long id, String adminUsername) {
        checkAdminAccess(adminUsername);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if ("ADMIN".equals(user.getRole())) {
            throw new RuntimeException("ОШИБКА: Нельзя заблокировать другого суперадмина!");
        }

        // НОВАЯ ПРОВЕРКА СОСТОЯНИЯ (Идемпотентность)
        if (!user.getIsActive()) {
            return "Внимание: Сотрудник " + user.getUsername() + " уже был заблокирован ранее.";
        }

        user.setIsActive(false);
        userRepository.save(user);

        actionLogService.logAction(adminUsername, "Заблокировал сотрудника ID: " + id);
        return "Сотрудник " + user.getUsername() + " успешно заблокирован.";
    }

    public String unblockUser(Long id, String adminUsername) {
        checkAdminAccess(adminUsername);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // НОВАЯ ПРОВЕРКА СОСТОЯНИЯ (Идемпотентность)
        if (user.getIsActive()) {
            return "Внимание: Сотрудник " + user.getUsername() + " уже активен.";
        }

        user.setIsActive(true);
        userRepository.save(user);

        actionLogService.logAction(adminUsername, "Разблокировал сотрудника ID: " + id);
        return "Сотрудник " + user.getUsername() + " успешно разблокирован.";
    }
}