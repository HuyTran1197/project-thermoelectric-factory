package com.example.project_backend_thermoelectric.repository;

import com.example.project_backend_thermoelectric.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    Optional<Tool> findByCode(String code);
}
