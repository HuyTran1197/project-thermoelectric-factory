package com.example.project_backend_thermoelectric.dto.materials_manager;

import lombok.Data;
import java.util.List;

@Data
public class FullMaterialExportDto {
    private Long workOrderId;
    private List<MaterialItem> consumables; // Danh sách vật tư tiêu hao chọn cấp
    private List<MaterialItem> replacements;// Danh sách phụ tùng thay thế chọn cấp

    @Data
    public static class MaterialItem {
        private Long materialId;
        private Integer quantity;
    }
}
