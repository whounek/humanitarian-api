package com.mchs.humanitarianapi.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "statuses")
@Data
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name; // Имя статуса
}