package com.mchs.humanitarianapi.services;

import com.mchs.humanitarianapi.models.Notification;
import com.mchs.humanitarianapi.models.User;
import com.mchs.humanitarianapi.repositories.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void send(User user, String message) {
        Notification note = new Notification();
        note.setUser(user);
        note.setMessage(message);
        notificationRepository.save(note);
    }

    public List<Notification> getMyNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}