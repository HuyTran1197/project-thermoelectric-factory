package com.example.project_backend_thermoelectric.service;

import com.example.project_backend_thermoelectric.entity.Employee;

import java.util.List;

public interface IEmployeeService {
    Employee createEmployee(Employee employee, Long departmentId, Long positionId);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    Employee updateEmployee(Long id, Employee employee, Long departmentId, Long positionId);
    void deleteEmployee(Long id);
//    void addWorkPositionToEmployee(Long employeeId, Long workPositionId);
//    void removeWorkPositionFromEmployee(Long employeeId, Long workPositionId);
}
