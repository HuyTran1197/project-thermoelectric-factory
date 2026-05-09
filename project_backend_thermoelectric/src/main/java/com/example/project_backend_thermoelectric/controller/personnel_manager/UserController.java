package com.example.project_backend_thermoelectric.controller.personnel_manager;

import com.example.project_backend_thermoelectric.dto.personnel_manager.CreateUserDto;
import com.example.project_backend_thermoelectric.dto.personnel_manager.UpdateUserDto;
import com.example.project_backend_thermoelectric.entity.User;
import com.example.project_backend_thermoelectric.service.personnel_manager.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping
    public User createUser(@RequestBody CreateUserDto request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        return userService.createUser(user, request.getEmployeeId());
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserDto request
    ) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "Delete user successfully";
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public String addRoleToUser(
            @PathVariable Long userId,
            @PathVariable Long roleId
    ) {
        userService.addRoleToUser(userId, roleId);
        return "Add role to user successfully";
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public String removeRoleFromUser(
            @PathVariable Long userId,
            @PathVariable Long roleId
    ) {
        userService.removeRoleFromUser(userId, roleId);
        return "Remove role from user successfully";
    }
}
