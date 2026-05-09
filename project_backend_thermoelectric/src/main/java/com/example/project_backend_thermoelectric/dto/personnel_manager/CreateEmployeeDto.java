package com.example.project_backend_thermoelectric.dto.personnel_manager;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeDto {
    private String fullName;
    private Long departmentId;
    private Long PositionId;
}
