package com.example.project_backend_thermoelectric.dto.work_orders;

public interface WorkOrderResponseDto {

    Long getId();

    String getCode();

    String getStatus();
    String getStatusDisplay();

    String getDescription();

    String getEquipment();
}
