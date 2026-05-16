package com.example.project_backend_thermoelectric.dto.operations_manager.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentParameterRequestDto {

    private Long parameterId;

    private String value;
}
