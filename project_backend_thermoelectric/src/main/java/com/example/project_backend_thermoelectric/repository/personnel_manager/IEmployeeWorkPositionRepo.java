package com.example.project_backend_thermoelectric.repository.personnel_manager;

import com.example.project_backend_thermoelectric.entity.EmployeeWorkPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IEmployeeWorkPositionRepo extends JpaRepository<EmployeeWorkPosition, Long> {
    List<EmployeeWorkPosition> findByEmployeeId(Long employeeId);
    void deleteByEmployeeIdAndWorkPositionId(Long employeeId, Long workPositionId);
    boolean existsByEmployeeIdAndWorkPositionId(Long employeeId, Long workPositionId);
}
