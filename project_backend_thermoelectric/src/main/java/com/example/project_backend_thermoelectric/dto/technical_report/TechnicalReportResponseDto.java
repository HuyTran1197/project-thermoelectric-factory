package com.example.project_backend_thermoelectric.dto.technical_report;

import java.time.LocalDateTime;

public interface TechnicalReportResponseDto {
    Long getId();

    String getWorkOrderCode();
    String getEquipmentCode();
    String getEquipmentName();

    LocalDateTime getCreatedAt();


}
