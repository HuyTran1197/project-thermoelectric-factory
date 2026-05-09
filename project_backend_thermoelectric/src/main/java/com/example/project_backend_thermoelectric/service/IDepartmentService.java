package com.example.project_backend_thermoelectric.service;

import com.example.project_backend_thermoelectric.entity.Department;

import java.util.List;

public interface IDepartmentService {
    Department createDepartment(Department department);
    List<Department> getAllDepartments();
    Department getDepartmentById(Long id);
    Department updateDepartment(Long id, Department department);
    void deleteDepartment(Long id);
}
