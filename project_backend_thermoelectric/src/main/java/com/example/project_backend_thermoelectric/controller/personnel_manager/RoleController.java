package com.example.project_backend_thermoelectric.controller.personnel_manager;

import com.example.project_backend_thermoelectric.dto.personnel_manager.RoleDto;
import com.example.project_backend_thermoelectric.entity.Role;
import com.example.project_backend_thermoelectric.service.personnel_manager.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin("*")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @PostMapping
    public Role createRole(@RequestBody RoleDto request) {
        Role role = new Role();
        role.setName(request.getName());

        return roleService.createRole(role);
    }

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    @PutMapping("/{id}")
    public Role updateRole(
            @PathVariable Long id,
            @RequestBody RoleDto request
    ) {
        Role role = new Role();
        role.setName(request.getName());

        return roleService.updateRole(id, role);
    }

    @DeleteMapping("/{id}")
    public String deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return "Delete role successfully";
    }
}
