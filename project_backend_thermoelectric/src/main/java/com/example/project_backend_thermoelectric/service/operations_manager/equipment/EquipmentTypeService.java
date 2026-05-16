package com.example.project_backend_thermoelectric.service.operations_manager.equipment;

import com.example.project_backend_thermoelectric.dto.operations_manager.request.EquipmentTypeRequestDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.response.EquipmentTypeDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.detail.EquipmentHeaderDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.detail.EquipmentParamDetailDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.detail.EquipmentTypeDetailResponse;
import com.example.project_backend_thermoelectric.entity.Domain;
import com.example.project_backend_thermoelectric.entity.EquipmentType;
import com.example.project_backend_thermoelectric.exception.DuplicateResourceException;
import com.example.project_backend_thermoelectric.exception.NotFoundResourceException;
import com.example.project_backend_thermoelectric.repository.operations_manager.IDomainRepo;
import com.example.project_backend_thermoelectric.repository.operations_manager.equipment.IEquipmentTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EquipmentTypeService implements IEquipmentTypeService {
    @Autowired
    private IEquipmentTypeRepo equipmentTypeRepo;
    @Autowired
    private IDomainRepo domainRepo;

    @Override
    public Page<EquipmentTypeDto> searchEquipmentTypeDto(
            String name,
            String domain,
            Pageable pageable
    ) {
        return equipmentTypeRepo.searchEquipmentTypeDto(
                name,
                domain,
                pageable
        );
    }


    @Override
    public List<EquipmentType> getAll() {
        return equipmentTypeRepo.findAll();
    }

    @Override
    public EquipmentType add(EquipmentTypeRequestDto dto) {
        boolean exists = equipmentTypeRepo.existsEquipmentTypeByNameAndDomain(dto.getName(),
                dto.getDomainId());
        if (exists) {
            throw new DuplicateResourceException("Loại thiết bị này đã tồn tại !!");
        }

        Domain domain = domainRepo.findById(dto.getDomainId()).orElseThrow(
                ()->new NotFoundResourceException("Không tìm thấy lĩnh vực !!")
        );

        EquipmentType equipmentType = new EquipmentType();
        equipmentType.setName(dto.getName());
        equipmentType.setDomain(domain);

        return equipmentTypeRepo.save(equipmentType);
    }

    @Override
    public EquipmentTypeDetailResponse detail(
            Long typeId,
            Long equipmentId
    ) {
        EquipmentHeaderDto header =
                equipmentTypeRepo.getHeader(typeId, equipmentId);

        if (header == null) {
            throw new NotFoundResourceException("Không tìm thấy dữ liệu !!");
        }

        String name = header.getName();
        String type = header.getType();
        String kks = header.getKks();

        List<EquipmentParamDetailDto> rows =
                equipmentTypeRepo.getParameters(typeId, equipmentId);

        Map<String, String> parameters = new LinkedHashMap<>();

        for (EquipmentParamDetailDto row : rows) {
            parameters.put(
                    row.getParameter(),
                    row.getValue()
            );
        }

        return new EquipmentTypeDetailResponse(
                name,
                type,
                kks,
                parameters
        );
    }

}
