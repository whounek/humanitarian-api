package com.mchs.humanitarianapi.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "standards")
@Data
public class Standard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связь с таблицей Типов ЧС
    @ManyToOne
    @JoinColumn(name = "disaster_type_id", nullable = false)
    private DisasterType disasterType;

    // Связь с таблицей Ресурсов
    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    // Сама норма на 1 человека (например, 2.0 литра)
    @Column(name = "quantity_per_person", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityPerPerson;
}