package com.mchs.humanitarianapi.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Кто сделал действие
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Какое действие (CREATE, UPDATE, DELETE)
    @Column(nullable = false)
    private String action;

    // В какой таблице (например, "standards" или "calculations")
    @Column(name = "entity_name", nullable = false)
    private String entityName;

    // ID измененной записи
    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}