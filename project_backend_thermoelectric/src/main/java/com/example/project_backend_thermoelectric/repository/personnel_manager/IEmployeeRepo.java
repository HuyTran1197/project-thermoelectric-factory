package com.example.project_backend_thermoelectric.repository.personnel_manager;

import com.example.project_backend_thermoelectric.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IEmployeeRepo extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartmentId(Long departmentId);
    List<Employee> findByPositionId(Long positionId);
}
