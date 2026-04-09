package com.mchs.humanitarianapi.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "shelters")
@Data
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    // НОВЫЕ ПОЛЯ ДЛЯ УМНОГО ПОИСКА
    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "address")
    private String address;
}