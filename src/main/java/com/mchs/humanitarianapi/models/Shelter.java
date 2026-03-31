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

    private Integer capacity;
}