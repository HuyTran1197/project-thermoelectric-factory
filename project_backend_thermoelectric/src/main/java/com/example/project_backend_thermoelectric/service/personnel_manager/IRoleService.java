package com.example.project_backend_thermoelectric.service.personnel_manager;

import com.example.project_backend_thermoelectric.entity.Role;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IRoleService {
    Role createRole(Role role);
    List<Role> getAllRoles();
    Page<Role> searchRoles(String keyword, int page, int size);
    List<Role> searchRoles(String keyword);
    Role getRoleById(Long id);
    Role updateRole(Long id, Role role);
    void deleteRole(Long id);
}
