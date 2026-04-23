package com.example.project_backend_thermoelectric.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "replacement_transactions")
public class ReplacementTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private Integer quantity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private ReplacementMaterial material;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}
