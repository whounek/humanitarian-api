package com.mchs.humanitarianapi.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "calculations")
@Data
public class Calculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Кто создал документ (младший сотрудник)
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Текущий статус документа (Черновик, На проверке и т.д.)
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    // Для какой ЧС делается расчет
    @ManyToOne
    @JoinColumn(name = "disaster_type_id", nullable = false)
    private DisasterType disasterType;

    @Column(name = "affected_people", nullable = false)
    private Integer affectedPeople; // Количество пострадавших

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}