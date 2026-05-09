package com.example.project_backend_thermoelectric.service;

import com.example.project_backend_thermoelectric.entity.Role;

import java.util.List;

public interface IRoleService {
    Role createRole(Role role);
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role updateRole(Long id, Role role);
    void deleteRole(Long id);
}
