package com.mchs.humanitarianapi.services;

import com.mchs.humanitarianapi.models.User;
import com.mchs.humanitarianapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String username, String password, String fullName) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // В реальном проекте пароль нужно шифровать
        user.setFullName(fullName);
        user.setIsActive(true);
        return userRepository.save(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // Проверяем, не заблокирован ли аккаунт
        if (!user.getIsActive()) {
            return "Ошибка: Аккаунт сотрудника заблокирован (уволен/отстранен)!";
        }

        // Проверяем пароль
        if (user.getPassword().equals(password)) {
            return "Успешная авторизация. Токен доступа сгенерирован.";
        }

        return "Ошибка: Неверный пароль";
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User blockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        user.setIsActive(false); // Мягкое удаление
        return userRepository.save(user);
    }
}