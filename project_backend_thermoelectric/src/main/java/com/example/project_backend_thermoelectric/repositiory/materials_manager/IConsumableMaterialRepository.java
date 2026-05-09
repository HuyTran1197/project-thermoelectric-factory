package com.example.project_backend_thermoelectric.repositiory.materials_manager;

import com.example.project_backend_thermoelectric.entity.ConsumableMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IConsumableMaterialRepository extends JpaRepository<ConsumableMaterial, Long>{
    @Query("SELECT c FROM ConsumableMaterial c " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.code) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ConsumableMaterial> searchConsumableMaterial(@Param("keyword") String keyword);
}
