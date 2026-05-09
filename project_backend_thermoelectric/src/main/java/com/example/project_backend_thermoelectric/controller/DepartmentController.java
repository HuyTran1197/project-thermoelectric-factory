package com.example.project_backend_thermoelectric.controller;

import com.example.project_backend_thermoelectric.dto.DepartmentDto;
import com.example.project_backend_thermoelectric.entity.Department;
import com.example.project_backend_thermoelectric.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin("*")
public class DepartmentController {
    @Autowired
    private IDepartmentService departmentService;

    @PostMapping
    public Department createDepartment(@RequestBody DepartmentDto request) {
        Department department = new Department();
        department.setName(request.getName());

        return departmentService.createDepartment(department);
    }

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @PutMapping("/{id}")
    public Department updateDepartment(
            @PathVariable Long id,
            @RequestBody DepartmentDto request
    ) {
        Department department = new Department();
        department.setName(request.getName());

        return departmentService.updateDepartment(id, department);
    }

    @DeleteMapping("/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return "Delete department successfully";
    }
}
