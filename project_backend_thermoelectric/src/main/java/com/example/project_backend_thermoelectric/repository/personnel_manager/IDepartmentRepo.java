package com.example.project_backend_thermoelectric.repository.personnel_manager;

import com.example.project_backend_thermoelectric.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDepartmentRepo extends JpaRepository<Department, Long> {
    boolean existsByName(String name);
    List<Department> findByNameContainingIgnoreCase(String name);
    Page<Department> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
