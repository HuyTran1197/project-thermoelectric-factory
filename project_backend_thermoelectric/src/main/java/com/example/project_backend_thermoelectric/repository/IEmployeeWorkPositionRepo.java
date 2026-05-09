package com.example.project_backend_thermoelectric.repositiory;

import com.example.project_backend_thermoelectric.entity.EmployeeWorkPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IEmployeeWorkPositionRepo extends JpaRepository<EmployeeWorkPosition, Long> {
    List<EmployeeWorkPosition> findByEmployeeId(Long employeeId);
    boolean existsByEmployeeIdAndWorkPositionId(Long employeeId, Long workPositionId);
    void deleteByEmployeeIdAndWorkPositionId(Long employeeId, Long workPositionId);
}
