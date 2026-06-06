package com.example.project_backend_thermoelectric.dto.personnel_manager;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeWorkPositionDto {
    private Long employeeId;
    private Long workPositionId;
    private String workPositionName;

}
