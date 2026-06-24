package com.example.project_backend_thermoelectric.dto.operations_manager.request;

import com.example.project_backend_thermoelectric.enums.EquipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentRequestDto {
    private String name;
    private String code;
    private Long systemId;
    private Long typeId;
    private List<EquipmentParameterRequestDto> parameters;
    private EquipmentStatus status;
}