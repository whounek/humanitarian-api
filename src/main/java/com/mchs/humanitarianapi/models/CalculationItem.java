package com.mchs.humanitarianapi.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "calculation_items")
@Data
public class CalculationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "calculation_id", nullable = false)
    private Calculation calculation;

    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;
}