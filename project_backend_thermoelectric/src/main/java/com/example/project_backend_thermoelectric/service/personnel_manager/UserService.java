package com.example.project_backend_thermoelectric.service.personnel_manager;


import com.example.project_backend_thermoelectric.entity.Employee;
import com.example.project_backend_thermoelectric.entity.Role;
import com.example.project_backend_thermoelectric.entity.User;
import com.example.project_backend_thermoelectric.entity.UserRole;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IEmployeeRepo;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IRoleRepo;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IUserRepo;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IUserRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepo userRepo;
    @Autowired
    private IEmployeeRepo employeeRepo;
    @Autowired
    private IRoleRepo roleRepo;
    @Autowired
    private IUserRoleRepo userRoleRepo;

    @Override
    public User createUser(User user, Long employeeId) {
        if(userRepo.existsByUsername(user.getUsername())){
            throw new RuntimeException("Username Đã được sử dụng!");
        }
        Employee employee = employeeRepo.findById(employeeId).orElseThrow(
                () -> new RuntimeException("Không tìm thấy nhân viên!"));
        user.setEmployee(employee);
        return userRepo.save(user);
    }
    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
    @Override
    public User getUserById(Long id) {
        return userRepo.findById(id).orElseThrow(
                () -> new RuntimeException("Không tìm thấy user!"));
    }
    @Override
    public User updateUser(Long id, User request) {
        User user = getUserById(id);
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        return userRepo.save(user);
    }
    @Override
    public void deleteUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("Không tìm thấy user!");
        }
        userRepo.deleteById(id);
    }
    @Override
    public void addRoleToUser(Long userId, Long roleId) {
        if(userRoleRepo.existsByUserIdAndRoleId(userId, roleId)){
            throw new RuntimeException("Username Đã được sử dụng!");
        }
        User user = getUserById(userId);
        Role  role = roleRepo.findById(roleId).orElseThrow(
                () -> new RuntimeException("Không tìm thấy role!"));
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleRepo.save(userRole);
    }
    @Override
    public void removeRoleFromUser(Long userId, Long roleId) {
        if(!userRoleRepo.existsByUserIdAndRoleId(userId, roleId)){
            throw new RuntimeException("Tên người dùng chưa được sử dụng!");
        }
        userRoleRepo.deleteByUserIdAndRoleId(userId, roleId);
    }
}
