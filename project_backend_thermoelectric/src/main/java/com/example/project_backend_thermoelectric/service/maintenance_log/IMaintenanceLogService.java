package com.example.project_backend_thermoelectric.service.maintenance_log;

import com.example.project_backend_thermoelectric.dto.maintenance_log.CreateMaintenanceLogDto;
import com.example.project_backend_thermoelectric.dto.maintenance_log.MaintenanceLogDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMaintenanceLogService {
    MaintenanceLogDto create(CreateMaintenanceLogDto dto);

    List<MaintenanceLogDto> getByWorkOrderId(Long workOrderId);

    List<MaintenanceLogDto> getByEquipmentId(Long equipmentId);
    Page<MaintenanceLogDto> search(String equipmentName, Pageable pageable);
}
