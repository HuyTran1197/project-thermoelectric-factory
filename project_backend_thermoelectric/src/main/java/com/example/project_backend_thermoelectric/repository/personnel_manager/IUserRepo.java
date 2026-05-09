package com.example.project_backend_thermoelectric.repository.personnel_manager;

import com.example.project_backend_thermoelectric.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IUserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
