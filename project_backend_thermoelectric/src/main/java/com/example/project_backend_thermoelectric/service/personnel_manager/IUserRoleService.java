package com.example.project_backend_thermoelectric.service.personnel_manager;

import com.example.project_backend_thermoelectric.entity.UserRole;

import java.util.List;

public interface IUserRoleService {
    UserRole assignRole(Long userId, Long roleId);
    void removeRole(Long userId, Long roleId);
    List<UserRole> getRolesByUser(Long userId);
}
