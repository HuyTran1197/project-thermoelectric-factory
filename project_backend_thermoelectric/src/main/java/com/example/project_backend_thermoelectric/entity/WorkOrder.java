package com.example.project_backend_thermoelectric.entity;

import com.example.project_backend_thermoelectric.enums.MaterialStatus;
import com.example.project_backend_thermoelectric.enums.WorkOrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @Column(
            unique = true,
            nullable = false
    )
    @NotBlank(message = "Không được bỏ trống")
    private String code;

    @Enumerated(EnumType.STRING)
    private WorkOrderStatus status;

    @Enumerated(EnumType.STRING)
    private MaterialStatus materialStatus;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "request_id",unique = true)
    private RepairOrder request;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}
