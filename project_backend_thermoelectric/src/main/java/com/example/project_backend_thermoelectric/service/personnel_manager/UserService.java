package com.example.project_backend_thermoelectric.service.personnel_manager;

import com.example.project_backend_thermoelectric.dto.personnel_manager.CreateUserDto;
import com.example.project_backend_thermoelectric.dto.personnel_manager.RoleDto;
import com.example.project_backend_thermoelectric.dto.personnel_manager.UpdateUserDto;
import com.example.project_backend_thermoelectric.dto.personnel_manager.UserDto;
import com.example.project_backend_thermoelectric.entity.Employee;
import com.example.project_backend_thermoelectric.entity.Role;
import com.example.project_backend_thermoelectric.entity.User;
import com.example.project_backend_thermoelectric.entity.UserRole;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IEmployeeRepo;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IRoleRepo;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IUserRepo;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IUserRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private IUserRepo userRepo;

    @Autowired
    private IEmployeeRepo employeeRepo;

    @Autowired
    private IRoleRepo roleRepo;

    @Autowired
    private IUserRoleRepo userRoleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(CreateUserDto dto) {

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {

            throw new RuntimeException(
                    "Mật khẩu xác nhận không khớp"
            );
        }

        if (userRepo.existsByUsername(dto.getUsername())) {

            throw new RuntimeException(
                    "Tên đăng nhập đã tồn tại"
            );
        }

        if (userRepo.existsByEmployeeId(dto.getEmployeeId())) {

            throw new RuntimeException(
                    "Nhân viên này đã có tài khoản"
            );
        }

        Employee employee = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Nhân viên không tồn tại"
                        )
                );

        User user = new User();

        user.setUsername(dto.getUsername());

        user.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );

        user.setEmployee(employee);

        user = userRepo.save(user);

        return mapToDTO(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UpdateUserDto dto) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        if (dto.getEmployeeId() != null) {
            Employee emp = employeeRepo.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));
            user.setEmployee(emp);
        }
        user = userRepo.save(user);
        return mapToDTO(user);
    }


    @Override
    public UserDto getUserDtoById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        return mapToDTO(user);
    }

    @Override
    public Page<UserDto> searchUsers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepo.findByUsernameContainingIgnoreCase(keyword, pageable);
        return userPage.map(this::mapToDTO);
    }

    @Transactional
    @Override
    public void addRoleToUser(Long userId, Long roleId) {
        if (userRoleRepo.existsByUserIdAndRoleId(userId, roleId)) return;
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Role role = roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("Role không tồn tại"));
        UserRole ur = new UserRole(); ur.setUser(user); ur.setRole(role);
        userRoleRepo.save(ur);
    }

    @Transactional
    @Override
    public void removeRoleFromUser(Long userId, Long roleId) {
        if (!userRoleRepo.existsByUserIdAndRoleId(userId, roleId)) return;
        userRoleRepo.deleteByUserIdAndRoleId(userId, roleId);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        for(UserRole ur : userRoleRepo.findByUserId(user.getId())) {
            userRoleRepo.delete(ur);
        }
        userRepo.delete(user);
    }


    private UserDto mapToDTO(User user) {
        List<RoleDto> roleDTOs = user.getRoles().stream()
                .map(ur -> new RoleDto(ur.getRole().getId(), ur.getRole().getName()))
                .collect(Collectors.toList());
        String employeeName = user.getEmployee() != null ? user.getEmployee().getFullName() : "chưa có nhân viên";
        return new UserDto(user.getId(), user.getUsername(), employeeName, roleDTOs);
    }
}