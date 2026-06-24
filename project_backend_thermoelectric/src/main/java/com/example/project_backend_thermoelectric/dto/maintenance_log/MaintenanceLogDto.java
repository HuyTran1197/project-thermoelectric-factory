package com.example.project_backend_thermoelectric.dto.maintenance_log;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceLogDto {
    private Long id;

    private Long workOrderId;
    private String workOrderCode;

    private Long equipmentId;
    private String equipmentName;
    private String equipmentCode;

    private String description;

    private LocalDateTime date;
}
