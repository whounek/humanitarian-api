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

    @ManyToOne
    @JoinColumn(name = "disaster_type_id", nullable = false)
    private DisasterType disasterType;

    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @Column(name = "quantity_per_person", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityPerPerson;

    // --- Добавили геттеры и сеттеры вручную, чтобы IDEA 100% их увидела ---
    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public BigDecimal getQuantityPerPerson() {
        return quantityPerPerson;
    }

    public void setQuantityPerPerson(BigDecimal quantityPerPerson) {
        this.quantityPerPerson = quantityPerPerson;
    }

    public DisasterType getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(DisasterType disasterType) {
        this.disasterType = disasterType;
    }
}