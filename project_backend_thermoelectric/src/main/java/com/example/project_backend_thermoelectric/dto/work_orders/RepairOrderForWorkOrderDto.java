package com.example.project_backend_thermoelectric.dto.work_orders;

import java.time.LocalDateTime;

public interface RepairOrderForWorkOrderDto {

    Long getId();
    String getTitle();
    String getDescription();
    String getEquipment();
    String getCreatedBy();
    LocalDateTime getCreatedAt();
    String getStatus();

    Integer getHasWorkOrder();
}
