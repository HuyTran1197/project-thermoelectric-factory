package com.example.project_backend_thermoelectric.dto.work_orders;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderDetailDto {

    private Long id;
    private String code;
    private String status;
    private String materialStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String createdBy;
    private String createdDepartment;
    private String createdPosition;

    // Repair Order
    private String repairTitle;
    private String repairDescription;

    // Equipment
    private String equipmentName;
    private String equipmentCode;
    private String systemName;

    // Nhân sự
    private List<AssignmentItemDto> assignments;

    // Vật tư
    private List<ConsumableDetailDto> consumables;
    private List<ReplacementDetailDto> replacements;
}
