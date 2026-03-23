package com.mchs.humanitarianapi.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "approval_history")
@Data
public class ApprovalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ссылка на сам документ
    @ManyToOne
    @JoinColumn(name = "calculation_id", nullable = false)
    private Calculation calculation;

    // Кто проверял (руководитель)
    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    // Статус, который присвоил руководитель (например, Отклонено)
    @ManyToOne
    @JoinColumn(name = "new_status_id", nullable = false)
    private Status newStatus;

    // Обязательный комментарий при отклонении
    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}