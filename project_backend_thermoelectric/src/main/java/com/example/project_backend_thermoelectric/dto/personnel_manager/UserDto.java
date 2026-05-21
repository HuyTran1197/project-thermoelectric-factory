package com.example.project_backend_thermoelectric.dto.personnel_manager;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String employeeName;
    private List<RoleDto> roles;
}
