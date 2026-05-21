package com.example.project_backend_thermoelectric.service.personnel_manager;

import com.example.project_backend_thermoelectric.entity.Role;
import com.example.project_backend_thermoelectric.entity.User;
import com.example.project_backend_thermoelectric.entity.UserRole;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IRoleRepo;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IUserRepo;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IUserRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserRoleService implements IUserRoleService {

    @Autowired
    private IUserRoleRepo userRoleRepo;

    @Autowired
    private IUserRepo userRepo;

    @Autowired
    private IRoleRepo roleRepo;

    @Override
    @Transactional
    public UserRole assignRole(Long userId, Long roleId) {
        if (userRoleRepo.existsByUserIdAndRoleId(userId, roleId))
            throw new RuntimeException("User đã có role này!");

        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Role role = roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("Role không tồn tại"));

        UserRole ur = new UserRole();
        ur.setUser(user);
        ur.setRole(role);

        return userRoleRepo.save(ur);
    }

    @Override
    @Transactional
    public void removeRole(Long userId, Long roleId) {
        if (!userRoleRepo.existsByUserIdAndRoleId(userId, roleId))
            throw new RuntimeException("User chưa có role này!");
        userRoleRepo.deleteByUserIdAndRoleId(userId, roleId);
    }

    @Override
    public List<UserRole> getRolesByUser(Long userId) {
        return userRoleRepo.findByUserId(userId);
    }
}