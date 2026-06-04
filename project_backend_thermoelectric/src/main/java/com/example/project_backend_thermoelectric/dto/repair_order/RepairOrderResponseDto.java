package com.example.project_backend_thermoelectric.dto.repair_order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RepairOrderResponseDto {

    private Long id;

    private String title;

    private String description;

    private String status;

    private Long equipmentId;

    private String equipmentCode;

    private String equipmentName;

    private String createdBy;

    private LocalDateTime createdAt;
}
