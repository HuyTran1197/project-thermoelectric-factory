package com.example.project_backend_thermoelectric.config.security;

import com.example.project_backend_thermoelectric.config.security.jwt.JwtAuthenticationFilter;
import com.example.project_backend_thermoelectric.config.security.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.Customizer;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/dashboard/**"
                        ).permitAll()
                        // ==========================
                        // PUBLIC
                        // ==========================
                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        // ==========================
                        // NHÂN SỰ
                        // ==========================
                        .requestMatchers(
                                "/api/users/**",
                                "/api/roles/**",
                                "/api/employees/**",
                                "/api/departments/**",
                                "/api/positions/**"
                        )
                        .hasAnyAuthority(
                                "ROLE_NHÂN SỰ",
                                "ROLE_QUẢN ĐỐC SỬA CHỮA",
                                "ROLE_TỔ TRƯỞNG",
                                "ROLE_ADMIN"
                        )

                        // ==========================
                        // QUẢN ĐỐC VẬN HÀNH
                        // ==========================
                        .requestMatchers(
                                "/api/equipments/**",
                                "/api/equipment-types/**",
                                "/api/system-equipments/**"
                        )
                        .hasAnyAuthority(
                                "ROLE_QUẢN ĐỐC VẬN HÀNH",
                                "ROLE_TRƯỞNG CA",
                                "ROLE_TRƯỞNG KÍP",
                                "ROLE_QUẢN ĐỐC SỬA CHỮA",
                                "ROLE_TỔ TRƯỞNG",
                                "ROLE_ADMIN"
                        )

                        // ==========================
                        // THỦ KHO VẬT TƯ
                        // ==========================
                        .requestMatchers(
                                "/api/consumable-materials/**",
                                "/api/consumable-transactions/**",
                                "/api/replacement-materials/**",
                                "/api/replacement-transactions/**"

                        )
                        .hasAnyAuthority(
                                "ROLE_THỦ KHO VẬT TƯ",
                                "ROLE_QUẢN ĐỐC SỬA CHỮA",
                                "ROLE_ADMIN"
                        )

                        // ==========================
                        // THỦ KHO CCDC
                        // ==========================
                        .requestMatchers(
                                "/api/tools/**",
                                "/api/tool-borrowings/**"
                        )
                        .hasAnyAuthority(
                                "ROLE_THỦ KHO CCDC",
                                "ROLE_ADMIN"
                        )

                        // ==========================
                        // TRƯỞNG CA / TRƯỞNG KÍP
                        // ==========================
                        .requestMatchers(
                                "/api/repair-orders/**"
                        )
                        .hasAnyAuthority(
                                "ROLE_TRƯỞNG CA",
                                "ROLE_TRƯỞNG KÍP",
                                "ROLE_ADMIN"
                        )

                        // ==========================
                        // QUẢN ĐỐC SỬA CHỮA / TỔ TRƯỞNG
                        // CẤP PHIẾU CÔNG TÁC
                        // ==========================
                        .requestMatchers(
                                "/api/work-orders/**"
                        )
                        .hasAnyAuthority(
                                "ROLE_QUẢN ĐỐC SỬA CHỮA",
                                "ROLE_TỔ TRƯỞNG",
                                "ROLE_ADMIN"
                        )

                        // ==========================
                        // QUẢN ĐỐC SỬA CHỮA / TỔ TRƯỞNG
                        // ==========================
                        .requestMatchers(
                                "/api/material-export/supply-slip",
                                "/api/material-export/request-material",
                                "/api/material-export/request-list"
                        )
                        .hasAnyAuthority(
                                "ROLE_QUẢN ĐỐC SỬA CHỮA",
                                "ROLE_TỔ TRƯỞNG",
                                "ROLE_ADMIN"
                        )

                        // ==========================
                        // THỦ KHO VẬT TƯ
                        // ==========================
                        .requestMatchers(
                                "/api/material-export/approve/**",
                                "/api/material-export/pending-list",
                                "/api/material-export/pending-count"

                        )
                        .hasAnyAuthority(
                                "ROLE_THỦ KHO VẬT TƯ",
                                "ROLE_ADMIN"
                        )

                        // ==========================
                        // XEM THÔNG TIN PHIẾU CẤP PHÁT
                        // ==========================
                        .requestMatchers(
                                "/api/material-export/work-order/**",
                                "/api/material-export/work-order-consumables/**",
                                "/api/material-export/work-order-replacements/**"
                        )
                        .hasAnyAuthority(
                                "ROLE_QUẢN ĐỐC SỬA CHỮA",
                                "ROLE_TỔ TRƯỞNG",
                                "ROLE_THỦ KHO VẬT TƯ",
                                "ROLE_ADMIN"
                        )

                        // ==========================
                        // BIÊN BẢN KỸ THUẬT
                        // ==========================
                        .requestMatchers(
                                "/api/technical-reports/**"
                        )
                        .hasAnyAuthority(
                                "ROLE_QUẢN ĐỐC SỬA CHỮA",
                                "ROLE_TỔ TRƯỞNG",
                                "ROLE_ADMIN"
                        )
                                // ==========================
                                // NGHIỆM THU - TRƯỞNG CA / TRƯỞNG KÍP
                        // ==========================
                                .requestMatchers(
                                        "/api/work-order-completion/**"
                                )
                                .hasAnyAuthority(
                                        "ROLE_TRƯỞNG CA",
                                        "ROLE_TRƯỞNG KÍP",
                                        "ROLE_QUẢN ĐỐC SỬA CHỮA",
                                        "ROLE_TỔ TRƯỞNG",
                                        "ROLE_ADMIN"
                                )
                        // ==========================
                        // CÒN LẠI PHẢI LOGIN
                        // ==========================
                        .anyRequest()
                        .authenticated()
                )

                .exceptionHandling(exception -> exception

                        // Chưa login hoặc token sai
                        .authenticationEntryPoint(
                                (request, response, authException) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_UNAUTHORIZED
                                    );

                                    response.setContentType(
                                            "application/json;charset=UTF-8"
                                    );

                                    response.getWriter().write("""
                    {
                        "status": 401,
                        "message": "Bạn chưa đăng nhập hoặc token không hợp lệ"
                    }
                    """);
                                }
                        )

                        // Đã login nhưng không đủ quyền
                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_FORBIDDEN
                                    );

                                    response.setContentType(
                                            "application/json;charset=UTF-8"
                                    );

                                    response.getWriter().write("""
                    {
                        "status": 403,
                        "message": "Bạn không có quyền truy cập chức năng này"
                    }
                    """);
                                }
                        )
                )
                .authenticationProvider(
                        authenticationProvider()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(
                customUserDetailsService
        );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new PasswordEncoder() {

            private final BCryptPasswordEncoder encoder =
                    new BCryptPasswordEncoder();

            @Override
            public String encode(CharSequence rawPassword) {
                return encoder.encode(rawPassword);
            }

            @Override
            public boolean matches(
                    CharSequence rawPassword,
                    String encodedPassword
            ) {

                if (encodedPassword.startsWith("$2a$")
                        || encodedPassword.startsWith("$2b$")) {

                    return encoder.matches(
                            rawPassword,
                            encodedPassword
                    );
                }

                return rawPassword.toString()
                        .equals(encodedPassword);
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {

        return config.getAuthenticationManager();
    }
}