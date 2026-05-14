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
            throw new RuntimeException("Phòng đã tồn tại!");
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
                () -> new RuntimeException("Không tìm thấy phòng"));
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
            throw new RuntimeException("Không tìm thấy phòng!");
        }
        departmentRepo.deleteById(id);
    }
}
