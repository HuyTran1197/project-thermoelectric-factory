package com.example.project_backend_thermoelectric.dto.operations_manager.response;

public interface EquipmentDto {
    Long getId();
    String getName();
    String getCode();
    String getSystemName();
    Long getTypeId();
    String getType();
    String getStatus();
    String getStatusDisplay();
}
