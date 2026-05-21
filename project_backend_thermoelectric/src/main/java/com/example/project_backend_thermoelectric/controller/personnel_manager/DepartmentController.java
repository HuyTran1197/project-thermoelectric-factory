package com.example.project_backend_thermoelectric.controller.personnel_manager;

import com.example.project_backend_thermoelectric.dto.personnel_manager.DepartmentDto;
import com.example.project_backend_thermoelectric.entity.Department;
import com.example.project_backend_thermoelectric.service.personnel_manager.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin("*")
public class DepartmentController {
    @Autowired
    private IDepartmentService departmentService;

    @GetMapping("/search")
    public Page<Department> searchDepartments(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        return departmentService.searchDepartments(keyword, page, size);
    }

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
