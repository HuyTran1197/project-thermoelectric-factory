package com.example.project_backend_thermoelectric.controller.personnel_manager;

import com.example.project_backend_thermoelectric.entity.UserRole;
import com.example.project_backend_thermoelectric.service.personnel_manager.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-roles")
@CrossOrigin("*")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/{userId}/{roleId}")
    public UserRole assignRole(@PathVariable Long userId, @PathVariable Long roleId) {
        return userRoleService.assignRole(userId, roleId);
    }

    @DeleteMapping("/{userId}/{roleId}")
    public String removeRole(@PathVariable Long userId, @PathVariable Long roleId) {
        userRoleService.removeRole(userId, roleId);
        return "Gỡ role thành công";
    }

    @GetMapping("/user/{userId}")
    public List<UserRole> getRolesByUser(@PathVariable Long userId) {
        return userRoleService.getRolesByUser(userId);
    }
}
