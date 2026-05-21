package com.example.project_backend_thermoelectric.repository.personnel_manager;

import com.example.project_backend_thermoelectric.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IRoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
    List<Role> findByNameContainingIgnoreCase(String name);
    Page<Role> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
