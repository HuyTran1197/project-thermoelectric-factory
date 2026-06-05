package com.example.project_backend_thermoelectric.dto.technical_report;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTechnicalReportDto {
    private Long workOrderId;
    private Long createdBy;
    private List<EquipmentReportDto> equipmentReports;
    private String conclusion; // Kết luận chung của biên bản
}
