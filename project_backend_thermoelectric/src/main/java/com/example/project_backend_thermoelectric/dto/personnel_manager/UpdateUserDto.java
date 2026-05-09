package com.example.project_backend_thermoelectric.dto.personnel_manager;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    private String username;
    private String password;
}
