package com.example.project_backend_thermoelectric.dto.operations_manager.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentTypeRequestDto {
    private String name;
    private Long domainId;
}
