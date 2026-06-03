package com.example.project_backend_thermoelectric.dto.auth;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {

    private String token;

    private String username;

    private List<String> roles;
}