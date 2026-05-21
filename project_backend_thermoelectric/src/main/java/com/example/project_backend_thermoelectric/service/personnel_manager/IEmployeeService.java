package com.example.project_backend_thermoelectric.service.personnel_manager;

import com.example.project_backend_thermoelectric.entity.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IEmployeeService {
    Employee createEmployee(Employee employee, Long departmentId, Long positionId);
    List<Employee> getAllEmployees();
    Page<Employee> searchEmployees(String keyword, int page, int size);
    List<Employee> searchEmployees(String keyword);
    Employee getEmployeeById(Long id);
    Employee updateEmployee(Long id, Employee employee, Long departmentId, Long positionId);
    void deleteEmployee(Long id);
//    void addWorkPositionToEmployee(Long employeeId, Long workPositionId);
//    void removeWorkPositionFromEmployee(Long employeeId, Long workPositionId);
}
