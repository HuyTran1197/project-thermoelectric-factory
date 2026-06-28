package com.example.project_backend_thermoelectric.service.operations_manager;

import com.example.project_backend_thermoelectric.dto.operations_manager.detail.EquipmentBySystemDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.request.EquipmentParameterRequestDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.request.EquipmentRequestDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.request.SystemRequestDto;
import com.example.project_backend_thermoelectric.entity.*;
import com.example.project_backend_thermoelectric.enums.EquipmentStatus;
import com.example.project_backend_thermoelectric.enums.WorkOrderStatus;
import com.example.project_backend_thermoelectric.exception.DuplicateResourceException;
import com.example.project_backend_thermoelectric.exception.NotFoundResourceException;
import com.example.project_backend_thermoelectric.repository.operations_manager.equipment.IEquipmentParameterRepo;
import com.example.project_backend_thermoelectric.repository.operations_manager.equipment.IEquipmentRepo;
import com.example.project_backend_thermoelectric.repository.operations_manager.ISystemEntityRepo;
import com.example.project_backend_thermoelectric.repository.operations_manager.equipment.IEquipmentTypeRepo;
import com.example.project_backend_thermoelectric.repository.operations_manager.equipment.IParameterDefinitionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private IParameterDefinitionRepo parameterDefinitionRepo;
    @Autowired
    private IEquipmentParameterRepo equipmentParameterRepo;

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
        boolean exists = equipmentRepo.existsEquipmentByCode(dto.getCode());
        if (exists){
            throw new DuplicateResourceException("Lỗi trùng mã KKS với thiết bị khác !!");
        }
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
        equipment.setStatus(EquipmentStatus.ACTIVE);
        equipment.setSystem(system);
        equipment.setType(type);

        Equipment savedEquipment = equipmentRepo.save(equipment);

        for (EquipmentParameterRequestDto p : dto.getParameters()) {
            ParameterDefinition parameter = parameterDefinitionRepo
                    .findById(p.getParameterId())
                    .orElseThrow(() ->
                            new NotFoundResourceException("Thông số không tồn tại !!"));
            EquipmentParameter equipmentParameter = new EquipmentParameter();

            equipmentParameter.setEquipment(savedEquipment);
            equipmentParameter.setParameter(parameter);
            equipmentParameter.setValue(p.getValue());

            equipmentParameterRepo.save(equipmentParameter);
        }

        return savedEquipment;
    }

    @Override
    public Page<EquipmentBySystemDto> getEquipmentsBySystem(Long systemId, String name, String code, String domain, Pageable pageable) {
        boolean existsSystem = systemEntityRepo.existsSystem(systemId);
        boolean existsOnEquipment = systemEntityRepo.existsSystemOnEquipment(systemId);

        if (!existsSystem){
            throw new NotFoundResourceException("Hệ thống không tồn tại !!");
        }if (!existsOnEquipment){
            throw new NotFoundResourceException("Thiết bị của hệ thống này chưa được vận hành !!");
        }

        return equipmentRepo.findEquipmentBySystemId(systemId,name,code,domain,pageable);
    }


}
