package com.example.project_backend_thermoelectric.service.operations_manager;

import com.example.project_backend_thermoelectric.dto.operations_manager.detail.EquipmentBySystemDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.request.EquipmentRequestDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.request.SystemRequestDto;
import com.example.project_backend_thermoelectric.entity.Equipment;
import com.example.project_backend_thermoelectric.entity.SystemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISystemEntityService {
    List<SystemEntity> findAll();
    SystemEntity add(SystemRequestDto dto);
    Equipment addEquipmentBySystemId(Long systemId, EquipmentRequestDto dto);
    Page<EquipmentBySystemDto> getEquipmentsBySystem(@Param("systemId") Long systemId,
                                                       @Param("searchName") String name,
                                                       @Param("searchCode") String code,
                                                       @Param("searchDomain") String domain,
                                                       Pageable pageable);
}
