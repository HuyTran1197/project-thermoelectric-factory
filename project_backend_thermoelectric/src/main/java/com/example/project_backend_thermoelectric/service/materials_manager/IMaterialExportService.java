package com.example.project_backend_thermoelectric.service.materials_manager;

import com.example.project_backend_thermoelectric.dto.materials_manager.FullMaterialExportDto;

import java.util.List;

public interface IMaterialExportService {
    void exportMaterialToWorkOrder(FullMaterialExportDto exportDTO);
    void approveAndReleaseMaterials(Long workOrderId);

    void approveSpecificMaterials(Long workOrderId, List<Long> approvedConsumableIds, List<Long> approvedReplacementIds);
}
