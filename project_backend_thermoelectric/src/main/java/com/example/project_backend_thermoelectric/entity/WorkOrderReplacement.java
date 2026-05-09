package com.example.project_backend_thermoelectric.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "work_order_replacements",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"work_order_id", "material_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderReplacement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    // FK tới work_order
    @ManyToOne
    @JoinColumn(name = "work_order_id", nullable = false)
    private WorkOrder workOrder;

    // FK tới replacement_material
    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private ReplacementMaterial material;
}