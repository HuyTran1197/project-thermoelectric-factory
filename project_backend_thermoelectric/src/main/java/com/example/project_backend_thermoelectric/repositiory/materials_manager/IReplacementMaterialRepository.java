package com.example.project_backend_thermoelectric.repositiory.materials_manager;

import com.example.project_backend_thermoelectric.entity.ReplacementMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReplacementMaterialRepository extends JpaRepository<ReplacementMaterial,Long> {
    @Query("SELECT r FROM ReplacementMaterial r " +
            "WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.code) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ReplacementMaterial> searchReplacementMaterial(@Param("keyword") String keyword);
}
