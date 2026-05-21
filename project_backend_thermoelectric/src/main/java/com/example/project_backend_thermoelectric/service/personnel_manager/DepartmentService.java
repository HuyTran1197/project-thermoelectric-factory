package com.example.project_backend_thermoelectric.service.personnel_manager;


import com.example.project_backend_thermoelectric.entity.Department;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IDepartmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Page<Department> searchDepartments(String keyword, int page, int size) {
        return departmentRepo.findByNameContainingIgnoreCase(
                keyword != null ? keyword : "",
                PageRequest.of(page, size)
        );
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepo.findAll();
    }

    @Override
    public List<Department> searchDepartments(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return departmentRepo.findAll();
        }
        return departmentRepo.findByNameContainingIgnoreCase(keyword);
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
        try {
            departmentRepo.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Không thể xóa phòng ban vì đang có nhân viên sử dụng");
        }
    }
}
