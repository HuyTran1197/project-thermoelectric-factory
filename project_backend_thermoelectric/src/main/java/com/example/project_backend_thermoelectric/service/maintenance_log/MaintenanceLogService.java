package com.example.project_backend_thermoelectric.service.maintenance_log;

import com.example.project_backend_thermoelectric.dto.maintenance_log.CreateMaintenanceLogDto;
import com.example.project_backend_thermoelectric.dto.maintenance_log.MaintenanceLogDto;
import com.example.project_backend_thermoelectric.entity.Equipment;
import com.example.project_backend_thermoelectric.entity.MaintenanceLog;
import com.example.project_backend_thermoelectric.entity.WorkOrder;
import com.example.project_backend_thermoelectric.repository.maintenance_log.IMaintenanceLogRepo;
import com.example.project_backend_thermoelectric.repository.operations_manager.equipment.IEquipmentRepo;
import com.example.project_backend_thermoelectric.repository.work_orders.IWorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceLogService implements IMaintenanceLogService {
    private final IMaintenanceLogRepo repository;
    private final IWorkOrderRepository workOrderRepository;

    @Override
    public MaintenanceLogDto create(CreateMaintenanceLogDto dto) {

        WorkOrder workOrder = workOrderRepository.findById(dto.getWorkOrderId())
                .orElseThrow(() -> new RuntimeException("WorkOrder không tồn tại"));

        // AUTO lấy equipment từ WorkOrder
        Equipment equipment = workOrder.getRequest().getEquipment();

        MaintenanceLog log = new MaintenanceLog();
        log.setWorkOrder(workOrder);
        log.setEquipment(equipment);
        log.setDescription(dto.getDescription());
        log.setDate(LocalDateTime.now());

        return map(repository.save(log));
    }

    @Override
    public Page<MaintenanceLogDto> search(String equipmentName, Pageable pageable) {

        return repository.search(equipmentName, pageable)
                .map(this::map);
    }

    private MaintenanceLogDto map(MaintenanceLog log) {

        MaintenanceLogDto dto = new MaintenanceLogDto();

        dto.setId(log.getId());

        dto.setWorkOrderId(log.getWorkOrder() != null ? log.getWorkOrder().getId() : null);
        dto.setWorkOrderCode(log.getWorkOrder() != null ? log.getWorkOrder().getCode() : null);

        dto.setEquipmentId(log.getEquipment().getId());
        dto.setEquipmentName(log.getEquipment().getName());
        dto.setEquipmentCode(log.getEquipment().getCode());

        dto.setDescription(log.getDescription());
        dto.setDate(log.getDate());

        return dto;
    }
}
