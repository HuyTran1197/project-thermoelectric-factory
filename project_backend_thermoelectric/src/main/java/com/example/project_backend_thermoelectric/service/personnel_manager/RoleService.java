package com.example.project_backend_thermoelectric.service.personnel_manager;


import com.example.project_backend_thermoelectric.entity.Role;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IRoleService {
    @Autowired
    private IRoleRepo roleRepo;

    @Override
    public Role createRole(Role role) {
        if(roleRepo.existsByName(role.getName())) {
            throw new RuntimeException("Role đã tồn tại!");
        }
        return roleRepo.save(role);
    }

    @Override
    public Page<Role> searchRoles(String keyword, int page, int size) {
        return roleRepo.findByNameContainingIgnoreCase(
                keyword != null ? keyword : "",
                PageRequest.of(page, size)
        );
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepo.findById(id).orElseThrow(
                () -> new RuntimeException("Không tìm thấy role!"));
    }

    @Override
    public List<Role> searchRoles(String keyword) {
        if(keyword == null || keyword.isEmpty()) {
            return roleRepo.findAll();
        }
        return roleRepo.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public Role updateRole(Long id, Role role) {
        Role existing = roleRepo.findById(id).orElseThrow(() -> new RuntimeException("Role không tồn tại"));
        existing.setName(role.getName());
        return roleRepo.save(existing);
    }

    @Override
    public void deleteRole(Long id) {
        if(!roleRepo.existsById(id)) {
            throw new RuntimeException("Không tìm thấy role!");
        }
        roleRepo.deleteById(id);
    }
}
