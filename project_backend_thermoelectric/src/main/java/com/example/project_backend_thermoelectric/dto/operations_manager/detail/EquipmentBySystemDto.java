package com.example.project_backend_thermoelectric.dto.operations_manager.detail;

public interface EquipmentBySystemDto {
    Long getId();
    String getSystemName();
    String getName();
    String getCode();
    Long getTypeId();
    String getDomain();
    String getStatus();
    String getStatusDisplay();
}
