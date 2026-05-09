package com.example.project_backend_thermoelectric.repository.operations_manager.equipment;

import com.example.project_backend_thermoelectric.dto.operations_manager.response.EquipmentDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.detail.EquipmentByTypeDto;
import com.example.project_backend_thermoelectric.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IEquipmentRepo extends JpaRepository<Equipment, Long> {
    @Query(value = """
            select 
                e.id as id,
                e.name as name,
                e.code as code,
                s.name as systemName,
                et.name as type,
                e.status as status
            from equipments e
            join systems s on s.id = e.system_id
            join equipment_types et on et.id = e.type_id
            where (:name = '' or e.name like :name)
            and (:code = '' or e.code like :code)
            and (:status = '' or e.status like :status)
            """,
            countQuery = """
                    select count(*)
                    from equipments e
                    join systems s on s.id = e.system_id
                    join equipment_types et on et.id = e.type_id
                    where (:name = '' or e.name like :name)
                    and (:code = '' or e.code like :code)
                    and (:status = '' or e.status like :status)
                    """,
            nativeQuery = true)
    Page<EquipmentDto> searchEquipmentDto(
            @Param("name") String name,
            @Param("code") String code,
            @Param("status") String status,
            Pageable pageable
    );

    @Query("select count(e) > 0 from Equipment e " +
            "where e.code = :code")
    boolean existsEquipmentByCode(@Param("code") String code);

    @Query("select e from Equipment e " +
            "where e.system.id = :systemId")
    List<Equipment> findEquipmentBySystemId(@Param("systemId") Long systemId);


    @Query("select count(e) > 0 from Equipment e " +
            "where e.type.id= :typeId")
    boolean existsEquipmentByTypeId(
            @Param("typeId") Long typeId
    );
    @Query(value = """
    select
        e.id as id,
        e.name as name,
        e.code as code
    from equipments e
    where e.type_id = :typeId
    """, nativeQuery = true)
    List<EquipmentByTypeDto> findByTypeId(
            @Param("typeId") Long typeId
    );
}
