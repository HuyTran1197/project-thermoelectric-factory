package com.example.project_backend_thermoelectric.dto.operations_manager.detail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentTypeDetailResponse {

    private String type;
    private String kks;
    private Map<String,String> parameters;
}
