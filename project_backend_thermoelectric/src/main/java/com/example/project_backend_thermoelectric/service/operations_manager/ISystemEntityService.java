package com.example.project_backend_thermoelectric.service.operations_manager;

import com.example.project_backend_thermoelectric.dto.operations_manager.request.EquipmentRequestDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.request.SystemRequestDto;
import com.example.project_backend_thermoelectric.entity.Equipment;
import com.example.project_backend_thermoelectric.entity.SystemEntity;

import java.util.List;

public interface ISystemEntityService {
    List<SystemEntity> findAll();
    SystemEntity add(SystemRequestDto dto);
    Equipment addEquipmentBySystemId(Long systemId, EquipmentRequestDto dto);
    List<Equipment> getEquipmentsBySystem(Long systemId);
}
