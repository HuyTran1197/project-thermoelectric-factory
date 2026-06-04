package com.example.project_backend_thermoelectric.dto.operations_manager.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentParameterEditDto {
    private Long parameterId;

    private String parameterName;
    private String unit;
    private String value;
}
