package com.example.project_backend_thermoelectric.dto.materials_manager;

import lombok.Data;
import java.util.List;

@Data
public class MaterialExportRequestDto {
    private Long workOrderId;
    private List<MaterialItemDTO> consumables;
    private List<MaterialItemDTO> replacements;

    @Data
    public static class MaterialItemDTO {
        private Long materialId;
        private Integer quantity;
    }
}
