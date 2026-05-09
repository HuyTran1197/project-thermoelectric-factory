package com.example.project_backend_thermoelectric.service.operations_manager.equipment;

import com.example.project_backend_thermoelectric.dto.operations_manager.response.ParameterDefinitionDto;
import com.example.project_backend_thermoelectric.entity.ParameterDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface IParameterDefinitionService {
    Page<ParameterDefinitionDto> searchAllParameterDefinition(@Param("name")String name,
                                                              Pageable pageable);

    ParameterDefinition add(ParameterDefinition parameterDefinition);
    ParameterDefinition edit(Long id,ParameterDefinition parameterDefinition);
}
