package com.example.project_backend_thermoelectric.repositiory;

import com.example.project_backend_thermoelectric.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IDepartmentRepo extends JpaRepository<Department, Long> {
    List<Department> findByType(String type);
    boolean existsByName(String name);
}
