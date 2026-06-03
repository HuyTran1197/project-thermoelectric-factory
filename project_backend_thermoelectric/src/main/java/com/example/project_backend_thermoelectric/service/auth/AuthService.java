package com.example.project_backend_thermoelectric.service.auth;

import com.example.project_backend_thermoelectric.config.security.jwt.JwtService;
import com.example.project_backend_thermoelectric.dto.auth.AuthResponseDto;
import com.example.project_backend_thermoelectric.dto.auth.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthResponseDto login(LoginRequestDto request) {

        try {

            var authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(),
                                    request.getPassword()
                            )
                    );

            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();

            String token =
                    jwtService.generateToken(userDetails);

            return new AuthResponseDto(
                    token,
                    userDetails.getUsername(),
                    userDetails.getAuthorities()
                            .stream()
                            .map(authority -> authority.getAuthority())
                            .toList()
            );

        } catch (BadCredentialsException e) {

            throw new RuntimeException(
                    "Tên đăng nhập hoặc mật khẩu không chính xác"
            );
        }
    }

    public AuthResponseDto getCurrentUser(UserDetails userDetails) {

        return new AuthResponseDto(
                null,
                userDetails.getUsername(),
                userDetails.getAuthorities()
                        .stream()
                        .map(authority -> authority.getAuthority())
                        .toList()
        );
    }
}