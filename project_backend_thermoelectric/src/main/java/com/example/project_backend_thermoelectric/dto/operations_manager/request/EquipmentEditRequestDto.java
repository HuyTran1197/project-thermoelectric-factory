package com.example.project_backend_thermoelectric.dto.operations_manager.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentEditRequestDto {
    private Long id;

    private String name;
    private String code;
    private Long systemId;
    private Long typeId;
    private String status;
    private List<EquipmentParameterEditDto> parameters;
}
