package com.example.project_backend_thermoelectric.repository.operations_manager.equipment;

import com.example.project_backend_thermoelectric.dto.operations_manager.response.EquipmentTypeDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.detail.EquipmentHeaderDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.detail.EquipmentParamDetailDto;
import com.example.project_backend_thermoelectric.entity.EquipmentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IEquipmentTypeRepo extends JpaRepository<EquipmentType,Long> {
    @Query(value = """
            select 
                e.id as id,
                e.name as name,
                d.name as domain
            from equipment_types e
            join domains d on d.id = e.domain_id
            where (:searchName = '' or e.name like concat('%', :searchName, '%'))
            and (:searchDomain = '' or d.name like concat('%', :searchDomain, '%'))
            """,
            countQuery = """
select count(*)
from equipment_types e
join domains d on d.id = e.domain_id
where (:searchName = '' or e.name like concat('%', :searchName, '%'))
and (:searchDomain = '' or d.name like concat('%', :searchDomain, '%'))
""",
            nativeQuery = true)
    Page<EquipmentTypeDto> searchEquipmentTypeDto(
            @Param("searchName") String name,
            @Param("searchDomain") String domain,
            Pageable pageable
    );

    @Query(value = "select count(e) > 0 from EquipmentType e " +
            "where e.name = :name " +
            "and e.domain.id = :domainId")
    boolean existsEquipmentTypeByNameAndDomain(@Param("name") String name,
                                      @Param("domainId") Long domainId);

    @Query(value = """
    select e.name as name,
        et.name as type,
        e.code as kks
    from equipment_types et
    join equipments e on e.type_id = et.id
    where et.id = :typeId
    and e.id = :equipmentId
    """, nativeQuery = true)
    EquipmentHeaderDto getHeader(
            @Param("typeId") Long typeId,
            @Param("equipmentId") Long equipmentId
    );

    @Query(value = """
        select 
            pd.name as parameter,
                    pd.unit as unit, 
            ep.value as value
        from parameter_definitions pd
        left join equipment_parameters ep
            on ep.parameter_id = pd.id
            and ep.equipment_id = :equipmentId
        where pd.type_id = :typeId
        """, nativeQuery = true)
    List<EquipmentParamDetailDto> getParameters(
            @Param("typeId") Long typeId,
            @Param("equipmentId") Long equipmentId
    );

}
