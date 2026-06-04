package com.example.project_backend_thermoelectric.controller.auth;

import com.example.project_backend_thermoelectric.dto.auth.AuthResponseDto;
import com.example.project_backend_thermoelectric.dto.auth.LoginRequestDto;
import com.example.project_backend_thermoelectric.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @RequestBody LoginRequestDto request
    ) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponseDto> me(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(
                authService.getCurrentUser(userDetails)
        );
    }
}