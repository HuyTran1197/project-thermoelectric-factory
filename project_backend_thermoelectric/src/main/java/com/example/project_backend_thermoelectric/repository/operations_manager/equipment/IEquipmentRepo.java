package com.example.project_backend_thermoelectric.repository.operations_manager.equipment;

import com.example.project_backend_thermoelectric.dto.operations_manager.detail.EquipmentBySystemDto;
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
                            et.id as typeId,
                et.name as type,
                e.status as status,
                case 
                when e.status = 'DANG_VAN_HANH' then 'Đang vận hành' 
                when e.status = 'DANG_SUA_CHUA' then 'Đang sửa chữa' 
                when e.status = 'DANG_DONG' then 'Đang đóng' 
                end as statusDisplay 
            from equipments e
            join systems s on s.id = e.system_id
            join equipment_types et on et.id = e.type_id
            where (:name = '' or e.name like :name)
            and (:code = '' or e.code like :code) 
            and (:system = '' or s.name like :system)
            and (:type = '' or et.name like :type)
            and (:status = '' or e.status like :status)
            """,
            countQuery = """
                            select count(*)
                            from equipments e
                            join systems s on s.id = e.system_id
                            join equipment_types et on et.id = e.type_id
                            where (:name = '' or e.name like :name)
                            and (:code = '' or e.code like :code)
                            and (:system = '' or s.name like :system)
                            and (:type = '' or et.name like :type)
                            and (:status = '' or e.status like :status)
                    """,
            nativeQuery = true)
    Page<EquipmentDto> searchEquipmentDto(
            @Param("name") String name,
            @Param("code") String code,
            @Param("system") String system,
            @Param("type") String type,
            @Param("status") String status,
            Pageable pageable
    );

    @Query("select count(e) > 0 from Equipment e " +
            "where e.code = :code")
    boolean existsEquipmentByCode(@Param("code") String code);

    @Query(value = "select e.id as id, " +
            "s.name as systemName, " +
            "e.name as name, " +
            "e.code as code, " +
            "e.type_id as typeId, " +
            "d.name as domain, " +
            "e.status as status " +
            "from equipments e " +
            "join systems s on s.id = e.system_id " +
            "join equipment_types et on et.id = e.type_id " +
            "join domains d on d.id = et.domain_id " +
            "where e.system_id = :systemId " +
            "and (:searchName is null or e.name like concat('%',:searchName,'%')) " +
            "and (:searchCode is null or e.code like concat('%',:searchCode,'%')) " +
            "and (:searchDomain is null or d.name like concat('%',:searchDomain,'%'))", nativeQuery = true)
    Page<EquipmentBySystemDto> findEquipmentBySystemId(@Param("systemId") Long systemId,
                                                       @Param("searchName") String name,
                                                       @Param("searchCode") String code,
                                                       @Param("searchDomain") String domain,
                                                       Pageable pageable);


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

    boolean existsEquipmentById(Long id);
}
