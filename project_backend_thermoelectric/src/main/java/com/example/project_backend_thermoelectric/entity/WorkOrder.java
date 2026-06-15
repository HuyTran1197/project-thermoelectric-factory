package com.example.project_backend_thermoelectric.entity;

import com.example.project_backend_thermoelectric.enums.MaterialStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "work_orders")
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    @Enumerated(EnumType.STRING)
    @Column(name = "material_status")
    private MaterialStatus materialStatus;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private RepairOrder request;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}
