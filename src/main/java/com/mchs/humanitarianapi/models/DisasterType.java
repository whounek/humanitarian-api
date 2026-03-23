package com.mchs.humanitarianapi.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "disaster_types")
@Data
public class DisasterType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Например: "Наводнение", "Землетрясение"
}