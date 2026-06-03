package com.example.project_backend_thermoelectric.repository.personnel_manager;

import com.example.project_backend_thermoelectric.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepo extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"roles", "roles.role"})
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmployeeId(Long employeeId);
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
