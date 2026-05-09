package com.example.project_backend_thermoelectric.repository;

import com.example.project_backend_thermoelectric.entity.ToolBorrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolBorrowingRepository extends JpaRepository<ToolBorrowing, Long> {
    List<ToolBorrowing> findByStatus(String status);
}
