package com.mchs.humanitarianapi.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "regions")
@Data
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // Климатический коэффициент (например, 1.5 для севера)
    @Column(name = "climate_coefficient", nullable = false, precision = 3, scale = 2)
    private BigDecimal climateCoefficient;
}