package com.example.project_backend_thermoelectric.service;

import com.example.project_backend_thermoelectric.entity.User;

import java.util.List;

public interface IUserService {
    User createUser(User user, Long employeeId);
    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    void addRoleToUser(Long userId, Long roleId);
    void removeRoleFromUser(Long userId, Long roleId);
}
