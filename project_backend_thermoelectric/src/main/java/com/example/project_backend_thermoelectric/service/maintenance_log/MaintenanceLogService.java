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
    private final IEquipmentRepo equipmentRepository;

    @Override
    public MaintenanceLogDto create(CreateMaintenanceLogDto dto) {

        WorkOrder workOrder = workOrderRepository.findById(dto.getWorkOrderId())
                .orElseThrow(() -> new RuntimeException("WorkOrder không tồn tại"));

        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Equipment không tồn tại"));

        MaintenanceLog log = new MaintenanceLog();

        log.setWorkOrder(workOrder);
        log.setEquipment(equipment);
        log.setDescription(dto.getDescription());
        log.setDate(LocalDateTime.now());

        MaintenanceLog saved = repository.save(log);

        return mapToDto(saved);
    }

    @Override
    public List<MaintenanceLogDto> getByWorkOrderId(Long workOrderId) {
        return repository.findByWorkOrderId(workOrderId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<MaintenanceLogDto> getByEquipmentId(Long equipmentId) {
        return repository.findByEquipmentId(equipmentId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public Page<MaintenanceLogDto> search(String equipmentName, Pageable pageable) {

        return repository.searchByEquipmentName(equipmentName, pageable)
                .map(this::mapToDto);
    }

    private MaintenanceLogDto mapToDto(MaintenanceLog log) {

        MaintenanceLogDto dto = new MaintenanceLogDto();

        dto.setId(log.getId());

        dto.setWorkOrderId(log.getWorkOrder().getId());
        dto.setWorkOrderCode(log.getWorkOrder().getCode());

        dto.setEquipmentId(log.getEquipment().getId());
        dto.setEquipmentName(log.getEquipment().getName());
        dto.setEquipmentCode(log.getEquipment().getCode());

        dto.setDescription(log.getDescription());

        dto.setDate(log.getDate());

        return dto;
    }
}
