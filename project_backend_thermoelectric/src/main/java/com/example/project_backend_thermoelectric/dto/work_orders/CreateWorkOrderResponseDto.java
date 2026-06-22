package com.example.project_backend_thermoelectric.dto.work_orders;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateWorkOrderResponseDto {
    private Long id;
    private String code;
}