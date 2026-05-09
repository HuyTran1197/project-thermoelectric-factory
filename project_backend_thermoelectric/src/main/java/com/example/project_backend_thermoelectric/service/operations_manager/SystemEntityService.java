package com.example.project_backend_thermoelectric.service.operations_manager;

import com.example.project_backend_thermoelectric.dto.operations_manager.request.EquipmentRequestDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.request.SystemRequestDto;
import com.example.project_backend_thermoelectric.entity.Equipment;
import com.example.project_backend_thermoelectric.entity.EquipmentType;
import com.example.project_backend_thermoelectric.entity.SystemEntity;
import com.example.project_backend_thermoelectric.exception.DuplicateResourceException;
import com.example.project_backend_thermoelectric.exception.NotFoundResourceException;
import com.example.project_backend_thermoelectric.repository.operations_manager.equipment.IEquipmentRepo;
import com.example.project_backend_thermoelectric.repository.operations_manager.ISystemEntityRepo;
import com.example.project_backend_thermoelectric.repository.operations_manager.equipment.IEquipmentTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SystemEntityService implements ISystemEntityService {
    @Autowired
    private ISystemEntityRepo systemEntityRepo;
    @Autowired
    private IEquipmentRepo equipmentRepo;
    @Autowired
    private IEquipmentTypeRepo equipmentTypeRepo;

    @Override
    public List<SystemEntity> findAll() {
        return systemEntityRepo.findAll();
    }

    @Override
    public SystemEntity add(SystemRequestDto dto) {
        boolean exists = systemEntityRepo.existsNameAndDescription(dto.getName());
        if (exists){
            throw new DuplicateResourceException("Hệ thống này đã tồn tại !!");
        }

        SystemEntity systemEntity = new SystemEntity();
        systemEntity.setName(dto.getName());
        systemEntity.setDescription(dto.getDescription());

        return systemEntityRepo.save(systemEntity);
    }

    @Override
    public Equipment addEquipmentBySystemId(Long systemId, EquipmentRequestDto dto) {
        SystemEntity system = systemEntityRepo.findById(systemId)
                .orElseThrow(() ->
                        new NotFoundResourceException("Hệ thống không tồn tại !!"));

        EquipmentType type = equipmentTypeRepo.findById(
                dto.getTypeId()
        ).orElseThrow(() ->
                new NotFoundResourceException("Không tìm thấy loại thiết bị !!"));

        Equipment equipment = new Equipment();

        equipment.setName(dto.getName());
        equipment.setCode(dto.getCode());
        equipment.setStatus(dto.getStatus());
        equipment.setSystem(system);
        equipment.setType(type);

        return equipmentRepo.save(equipment);
    }

    @Override
    public List<Equipment> getEquipmentsBySystem(Long systemId) {
        boolean existsSystem = systemEntityRepo.existsSystem(systemId);
        boolean existsOnEquipment = systemEntityRepo.existsSystemOnEquipment(systemId);

        if (!existsSystem){
            throw new NotFoundResourceException("Hệ thống không tồn tại !!");
        }if (!existsOnEquipment){
            throw new NotFoundResourceException("Thiết bị của hệ thống này chưa được vận hành !!");
        }

        return equipmentRepo.findEquipmentBySystemId(systemId);
    }

}
