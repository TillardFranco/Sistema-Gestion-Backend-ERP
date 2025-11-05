package com.example.farmaser.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "audit_log", indexes = {
    @Index(name = "idx_entity_type_id", columnList = "entity_type,entity_id"),
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_action", columnList = "action"),
    @Index(name = "idx_timestamp", columnList = "timestamp")
})
public class AuditLogEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_type", nullable = false, length = 100)
    private String entityType; // Ej: "Product", "Sale", "Customer", etc.

    @Column(name = "entity_id")
    private Long entityId; // ID de la entidad afectada

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 50)
    private ActionType action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // Usuario que realizó la acción

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue; // Valor anterior (JSON string)

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue; // Valor nuevo (JSON string)

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // Descripción de la acción

    @Column(name = "ip_address", length = 45)
    private String ipAddress; // IP del usuario

    @Column(name = "timestamp", nullable = false, updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
}

