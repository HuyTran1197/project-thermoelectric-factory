package com.example.project_backend_thermoelectric.repository.personnel_manager;

import com.example.project_backend_thermoelectric.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPositionRepo extends JpaRepository<Position, Long> {
    boolean existsByName(String name);
}
