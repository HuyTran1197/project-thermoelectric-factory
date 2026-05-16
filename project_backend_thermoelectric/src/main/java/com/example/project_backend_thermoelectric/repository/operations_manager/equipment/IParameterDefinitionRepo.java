package com.example.project_backend_thermoelectric.repository.operations_manager.equipment;

import com.example.project_backend_thermoelectric.dto.operations_manager.response.ParameterDefinitionDto;
import com.example.project_backend_thermoelectric.entity.ParameterDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IParameterDefinitionRepo extends JpaRepository<ParameterDefinition,Long> {
    @Query(value = "select name, " +
            "unit, type_id as type " +
            "from parameter_definitions  " +
            "where (:name = '' or name like :name)",nativeQuery = true)
    Page<ParameterDefinitionDto> searchAllParameterDefinition(@Param("name")String name,
                                           Pageable pageable);


    @Query(value = "select count(p)>0 from ParameterDefinition p " +
            "where p.name = :name " +
            "and p.type.id = :typeId")
    boolean existsParameterDefinitionByNameAndType(@Param("name") String name,
                                                   @Param("typeId") Long typeId);

    @Query(value = "select * from parameter_definitions p " +
            "where p.type_id = :typeId",nativeQuery = true)
    List<ParameterDefinition> findParameterDefinitionByTypeId(@Param("typeId") Long typeId);
}
