package com.example.project_backend_thermoelectric.repository.tool;

import com.example.project_backend_thermoelectric.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    Optional<Tool> findByCode(String code);

    @Query("SELECT t FROM Tool t WHERE " +
            "(:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:code IS NULL OR LOWER(t.code) LIKE LOWER(CONCAT('%', :code, '%'))) AND " +
            "(:type IS NULL OR LOWER(t.type) LIKE LOWER(CONCAT('%', :type, '%')))")
    List<Tool> searchTools(@Param("name") String name,
                           @Param("code") String code,
                           @Param("type") String type);
}
