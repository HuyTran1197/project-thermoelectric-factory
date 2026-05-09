package com.example.project_backend_thermoelectric.dto.personnel_manager;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeDto {
    private String fullName;
    private Long departmentId;
    private Long PositionId;
}
