package com.example.project_backend_thermoelectric.service.operations_manager.equipment;

import com.example.project_backend_thermoelectric.dto.operations_manager.request.EquipmentParameterRequestDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.request.EquipmentRequestDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.response.EquipmentDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.detail.EquipmentByTypeDto;
import com.example.project_backend_thermoelectric.entity.*;
import com.example.project_backend_thermoelectric.exception.DuplicateResourceException;
import com.example.project_backend_thermoelectric.exception.NotFoundResourceException;
import com.example.project_backend_thermoelectric.repository.operations_manager.ISystemEntityRepo;
import com.example.project_backend_thermoelectric.repository.operations_manager.equipment.IEquipmentParameterRepo;
import com.example.project_backend_thermoelectric.repository.operations_manager.equipment.IEquipmentRepo;
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
public class EquipmentService implements IEquipmentService{
    @Autowired
    private IEquipmentRepo equipmentRepo;
    @Autowired
    private IEquipmentTypeRepo equipmentTypeRepo;
    @Autowired
    private ISystemEntityRepo systemEntityRepo;
    @Autowired
    private IParameterDefinitionRepo parameterDefinitionRepo;
    @Autowired
    private IEquipmentParameterRepo equipmentParameterRepo;

    @Override
    public Page<EquipmentDto> searchEquipmentDto(String name, String code, String status,String system,String type, Pageable pageable) {
        return equipmentRepo.searchEquipmentDto("%"+name+"%",
                "%"+code+"%",
                "%"+system+"%",
                "%"+type+"%",
                "%"+status+"%",pageable);
    }

    @Override
    public Equipment add(EquipmentRequestDto dto) {
        boolean exists = equipmentRepo.existsEquipmentByCode(dto.getCode());
        if (exists){
            throw new DuplicateResourceException("Lỗi trùng mã KKS với thiết bị khác !!");
        }

        SystemEntity system = systemEntityRepo.findById(dto.getSystemId())
                .orElseThrow(() -> new NotFoundResourceException("Hệ thống không tồn tại"));

        EquipmentType type = equipmentTypeRepo.findById(dto.getTypeId())
                .orElseThrow(() -> new NotFoundResourceException("Loại thiết bị không tồn tại"));

        Equipment equipment = new Equipment();

        equipment.setName(dto.getName());
        equipment.setCode(dto.getCode());
        equipment.setStatus(dto.getStatus());
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
    public Equipment edit(Long id, EquipmentRequestDto dto) {
        Equipment current = equipmentRepo.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Không tìm thấy thiết bị !!"));

        SystemEntity system = systemEntityRepo.findById(dto.getSystemId())
                .orElseThrow(() -> new NotFoundResourceException("Hệ thống không tồn tại"));

        EquipmentType type = equipmentTypeRepo.findById(dto.getTypeId())
                .orElseThrow(() -> new NotFoundResourceException("Loại thiết bị không tồn tại"));

        current.setName(dto.getName());
        current.setCode(dto.getCode());
        current.setSystem(system);
        current.setType(type);
        current.setStatus(dto.getStatus());

        return equipmentRepo.save(current);
    }

    @Override
    public Equipment findById(Long id) {
        return equipmentRepo.findById(id).orElseThrow(
                () -> new NotFoundResourceException("Không tìm thấy thiết bị")
        );
    }

    @Override
    public List<EquipmentByTypeDto> getEquipmentsByType(Long typeId) {
        boolean exists = equipmentRepo.existsEquipmentByTypeId(typeId);
        if (!exists) throw new NotFoundResourceException("Loại thiết bị chưa được vận hành !!");

        return equipmentRepo.findByTypeId(typeId);
    }

}
