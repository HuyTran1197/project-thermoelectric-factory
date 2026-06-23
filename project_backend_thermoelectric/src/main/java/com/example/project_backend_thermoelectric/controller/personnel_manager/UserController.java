package com.example.project_backend_thermoelectric.controller.personnel_manager;

import com.example.project_backend_thermoelectric.dto.personnel_manager.CreateUserDto;
import com.example.project_backend_thermoelectric.dto.personnel_manager.UpdateUserDto;
import com.example.project_backend_thermoelectric.dto.personnel_manager.UserDto;
import com.example.project_backend_thermoelectric.entity.User;
import com.example.project_backend_thermoelectric.service.personnel_manager.IUserRoleService;
import com.example.project_backend_thermoelectric.service.personnel_manager.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping
    public Page<UserDto> searchUsers(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return userService.searchUsers(keyword, page, size);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserDtoById(id);
    }

    @PostMapping
    public UserDto createUser(@RequestBody CreateUserDto dto) {
        return userService.createUser(dto);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UpdateUserDto dto) {
        return userService.updateUser(id, dto);
    }


    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "Xóa user thành công";
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public String assignRole(@PathVariable Long userId, @PathVariable Long roleId) {
        userService.addRoleToUser(userId, roleId);
        return "Gán role thành công";
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public String removeRole(@PathVariable Long userId, @PathVariable Long roleId) {
        userService.removeRoleFromUser(userId, roleId);
        return "Gỡ role thành công";
    }
}
