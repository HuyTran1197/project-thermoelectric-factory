package com.example.project_backend_thermoelectric.service.materials_manager;

import com.example.project_backend_thermoelectric.dto.materials_manager.FullMaterialExportDto;

public interface IMaterialExportService {
    void exportMaterialToWorkOrder(FullMaterialExportDto exportDTO);
    void approveAndReleaseMaterials(Long workOrderId, Long warehouseStaffId);
}
