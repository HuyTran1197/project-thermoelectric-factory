package com.example.project_backend_thermoelectric.dto.operations_manager.detail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentParamResponse {
    private String parameter;
    private String unit;
    private String value;
}
