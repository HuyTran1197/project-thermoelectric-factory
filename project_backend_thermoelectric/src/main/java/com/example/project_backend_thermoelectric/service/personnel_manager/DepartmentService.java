package com.example.project_backend_thermoelectric.service.personnel_manager;


import com.example.project_backend_thermoelectric.entity.Department;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IDepartmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService implements IDepartmentService {
    @Autowired
    private IDepartmentRepo departmentRepo;

    @Override
    public Department createDepartment(Department department) {
        if(departmentRepo.existsByName(department.getName())){
            throw new RuntimeException("Department already exists");
        }
        return departmentRepo.save(department);
    }
    @Override
    public List<Department> getAllDepartments() {
        return departmentRepo.findAll();
    }
    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepo.findById(id).orElseThrow(
                () -> new RuntimeException("Department not found"));
    }
    @Override
    public Department updateDepartment(Long id, Department request) {
        Department department = getDepartmentById(id);
        department.setName(request.getName());
        return departmentRepo.save(department);
    }
    @Override
    public void deleteDepartment(Long id) {
        if(!departmentRepo.existsById(id)){
            throw new RuntimeException("Department not found");
        }
        departmentRepo.deleteById(id);
    }
}
