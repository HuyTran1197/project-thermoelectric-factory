package com.example.project_backend_thermoelectric.repository.materials_manager;

import com.example.project_backend_thermoelectric.entity.ConsumableMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IConsumableMaterialRepository extends JpaRepository<ConsumableMaterial, Long>{
    @Query("""
    SELECT c FROM ConsumableMaterial c
    WHERE (:code IS NULL OR :code = ''
           OR LOWER(c.code) LIKE LOWER(CONCAT('%', :code, '%')))
    AND (:name IS NULL OR :name = ''
           OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
""")
    Page<ConsumableMaterial> searchConsumableMaterial(
            @Param("code") String code,
            @Param("name") String name,
            Pageable pageable
    );
}
