package com.example.project_backend_thermoelectric.repository.operations_manager;

import com.example.project_backend_thermoelectric.entity.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDomainRepo extends JpaRepository<Domain,Long> {
}
