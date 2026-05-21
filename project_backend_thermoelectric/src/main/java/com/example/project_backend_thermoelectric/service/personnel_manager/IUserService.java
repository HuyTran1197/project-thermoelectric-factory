package com.example.project_backend_thermoelectric.service.personnel_manager;

import com.example.project_backend_thermoelectric.dto.personnel_manager.CreateUserDto;
import com.example.project_backend_thermoelectric.dto.personnel_manager.UserDto;
import com.example.project_backend_thermoelectric.entity.User;
import org.springframework.data.domain.Page;

public interface IUserService {
    UserDto createUser(CreateUserDto dto);
    UserDto getUserDtoById(Long id);
    Page<UserDto> searchUsers(String keyword, int page, int size);
    void addRoleToUser(Long userId, Long roleId);
    void removeRoleFromUser(Long userId, Long roleId);
}
