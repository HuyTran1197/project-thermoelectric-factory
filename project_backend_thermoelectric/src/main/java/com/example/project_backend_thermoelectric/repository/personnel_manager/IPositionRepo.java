package com.example.project_backend_thermoelectric.repository.personnel_manager;

import com.example.project_backend_thermoelectric.entity.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPositionRepo extends JpaRepository<Position, Long> {
    boolean existsByName(String name);
    List<Position> findByNameContainingIgnoreCase(String name);
    Page<Position> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
