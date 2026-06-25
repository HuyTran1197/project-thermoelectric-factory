package com.example.project_backend_thermoelectric.dto.maintenance_log;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMaintenanceLogDto {
    private Long workOrderId;

    private String description;
}
