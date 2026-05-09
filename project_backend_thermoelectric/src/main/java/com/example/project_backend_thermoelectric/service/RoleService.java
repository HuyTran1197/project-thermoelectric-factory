package com.example.project_backend_thermoelectric.service;


import com.example.project_backend_thermoelectric.entity.Role;
import com.example.project_backend_thermoelectric.repositiory.IRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IRoleService {
    @Autowired
    private IRoleRepo roleRepo;

    @Override
    public Role createRole(Role role) {
        if(roleRepo.existsByName(role.getName())) {
            throw new RuntimeException("Role already exists");
        }
        return roleRepo.save(role);
    }
    @Override
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }
    @Override
    public Role getRoleById(Long id) {
        return roleRepo.findById(id).orElseThrow(
                () -> new RuntimeException("Role id not found"));
    }
    @Override
    public Role updateRole(Long id, Role request) {
        Role role = getRoleById(id);
        role.setName(request.getName());
        return roleRepo.save(role);
    }
    @Override
    public void deleteRole(Long id) {
        if(!roleRepo.existsById(id)) {
            throw new RuntimeException("Role id not found");
        }
        roleRepo.deleteById(id);
    }
}
