package com.example.project_backend_thermoelectric.service.operations_manager.equipment;

import com.example.project_backend_thermoelectric.dto.operations_manager.response.ParameterDefinitionDto;
import com.example.project_backend_thermoelectric.entity.EquipmentType;
import com.example.project_backend_thermoelectric.entity.ParameterDefinition;
import com.example.project_backend_thermoelectric.exception.DuplicateResourceException;
import com.example.project_backend_thermoelectric.exception.NotFoundResourceException;
import com.example.project_backend_thermoelectric.repository.operations_manager.equipment.IEquipmentTypeRepo;
import com.example.project_backend_thermoelectric.repository.operations_manager.equipment.IParameterDefinitionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ParameterDefinitionService implements IParameterDefinitionService{
    @Autowired
    private IParameterDefinitionRepo parameterDefinitionRepo;
    @Autowired
    private IEquipmentTypeRepo equipmentTypeRepo;

    @Override
    public Page<ParameterDefinitionDto> searchAllParameterDefinition(String name, Pageable pageable) {
        return parameterDefinitionRepo.searchAllParameterDefinition("%"+name+"%",pageable);
    }


    @Override
    public ParameterDefinition add(ParameterDefinition parameterDefinition) {
        boolean exists = parameterDefinitionRepo.existsParameterDefinitionByNameAndType(
                parameterDefinition.getName(),
                parameterDefinition.getType().getId()
        );
        if (exists) throw new DuplicateResourceException("Lỗi trùng thông số thiết bị");

        EquipmentType type = equipmentTypeRepo.findById(
                parameterDefinition.getType().getId()
        ).orElseThrow(() ->
                new NotFoundResourceException("Không tìm thấy loại thiết bị !!"));

        parameterDefinition.setType(type);
        return parameterDefinitionRepo.save(parameterDefinition);
    }

    @Override
    public ParameterDefinition edit(Long id, ParameterDefinition parameterDefinition) {
        ParameterDefinition current = parameterDefinitionRepo.findById(id).orElseThrow(
                () -> new NotFoundResourceException("Không tìm thấy thông số")
        );
        EquipmentType type = equipmentTypeRepo.findById(
                parameterDefinition.getType().getId()
        ).orElseThrow(() ->
                new NotFoundResourceException("Không tìm thấy loại thiết bị !!"));

        current.setName(parameterDefinition.getName());
        current.setUnit(parameterDefinition.getUnit());
        current.setType(type);
        return parameterDefinitionRepo.save(current);
    }
}
