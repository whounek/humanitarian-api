package com.mchs.humanitarianapi.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "calculations")
@Data
@Audited // <-- ВКЛЮЧАЕМ ПОЛНЫЙ АУДИТ ДОКУМЕНТА
public class Calculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Говорим Envers: сохраняй ID автора, но не требуй аудита самой таблицы users
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "disaster_type_id", nullable = false)
    private DisasterType disasterType;

    @Column(name = "affected_people", nullable = false)
    private Integer affectedPeople;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "manager_comment")
    private String managerComment;

    public String getManagerComment() { return managerComment; }
    public void setManagerComment(String managerComment) { this.managerComment = managerComment; }
}