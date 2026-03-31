package com.mchs.humanitarianapi.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "resource_categories")
@Data
public class ResourceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}