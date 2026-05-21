package com.example.project_backend_thermoelectric.service.personnel_manager;

import com.example.project_backend_thermoelectric.entity.Department;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IDepartmentService {
    Department createDepartment(Department department);
    Page<Department> searchDepartments(String keyword, int page, int size);
    List<Department> searchDepartments(String keyword);
    List<Department> getAllDepartments();
    Department getDepartmentById(Long id);
    Department updateDepartment(Long id, Department department);
    void deleteDepartment(Long id);
}
