package com.mchs.humanitarianapi.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import java.math.BigDecimal;

@Entity
@Table(name = "calculation_items")
@Data
@Audited // <-- ВКЛЮЧАЕМ АУДИТ ПОЗИЦИЙ РАСЧЕТА
public class CalculationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Сам расчет тоже аудируется, поэтому здесь NOT_AUDITED не нужен
    @ManyToOne
    @JoinColumn(name = "calculation_id", nullable = false)
    private Calculation calculation;

    // Ресурс (Вода, Еда) - это статический справочник
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;
}