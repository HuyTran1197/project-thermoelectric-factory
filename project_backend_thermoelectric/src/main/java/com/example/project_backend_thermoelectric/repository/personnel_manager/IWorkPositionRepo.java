package com.example.project_backend_thermoelectric.repository.personnel_manager;

import com.example.project_backend_thermoelectric.entity.WorkPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWorkPositionRepo extends JpaRepository<WorkPosition, Long> {
    boolean existsByName(String name);
}
