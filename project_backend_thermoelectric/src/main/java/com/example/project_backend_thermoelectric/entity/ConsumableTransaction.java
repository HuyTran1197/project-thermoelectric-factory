package com.example.project_backend_thermoelectric.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consumable_transactions")
public class ConsumableTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private Integer quantity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private ConsumableMaterial material;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}
