package com.example.project_backend_thermoelectric.repository.materials_manager;
import com.example.project_backend_thermoelectric.entity.ReplacementMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IReplacementMaterialRepository extends JpaRepository<ReplacementMaterial,Long> {
    @Query("""
    SELECT r FROM ReplacementMaterial r
    WHERE (:code IS NULL OR :code = ''
           OR LOWER(r.code) LIKE LOWER(CONCAT('%', :code, '%')))
    AND (:name IS NULL OR :name = ''
           OR LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')))
""")
    Page<ReplacementMaterial> searchReplacementMaterial(
            @Param("code") String code,
            @Param("name") String name,
            Pageable pageable
    );
}
