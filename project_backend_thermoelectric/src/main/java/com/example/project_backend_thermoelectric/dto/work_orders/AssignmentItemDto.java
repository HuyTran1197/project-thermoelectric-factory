package com.example.project_backend_thermoelectric.dto.work_orders;

import com.example.project_backend_thermoelectric.enums.WorkOrderRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentItemDto {

    private Long employeeId;

    private String employeeName;

    private WorkOrderRole role;
}