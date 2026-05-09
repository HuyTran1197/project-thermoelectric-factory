package com.example.project_backend_thermoelectric.service.operations_manager.equipment;

import com.example.project_backend_thermoelectric.dto.operations_manager.request.EquipmentRequestDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.response.EquipmentDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.detail.EquipmentByTypeDto;
import com.example.project_backend_thermoelectric.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IEquipmentService {
    Page<EquipmentDto> searchEquipmentDto(@Param("searchName") String name,
                                          @Param("searchCode") String code,
                                          @Param("searchStatus") String status,
                                          Pageable pageable);
    Equipment add (EquipmentRequestDto equipment);
    Equipment edit (Long id, EquipmentRequestDto dto);
    Equipment findById (Long id);

    List<EquipmentByTypeDto> getEquipmentsByType(Long typeId);
}
