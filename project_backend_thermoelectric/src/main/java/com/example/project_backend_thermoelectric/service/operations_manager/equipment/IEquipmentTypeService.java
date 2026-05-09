package com.example.project_backend_thermoelectric.service.operations_manager.equipment;

import com.example.project_backend_thermoelectric.dto.operations_manager.request.EquipmentTypeRequestDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.response.EquipmentTypeDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.detail.EquipmentTypeDetailResponse;
import com.example.project_backend_thermoelectric.entity.EquipmentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IEquipmentTypeService {
    Page<EquipmentTypeDto> searchEquipmentTypeDto(
            @Param("searchName") String name,
            @Param("searchDomain") String domain,
            Pageable pageable
    );
    List<EquipmentType> getAll();
    EquipmentType add(EquipmentTypeRequestDto dto);

    EquipmentTypeDetailResponse detail(
            Long typeId,
            Long equipmentId
    );
}
