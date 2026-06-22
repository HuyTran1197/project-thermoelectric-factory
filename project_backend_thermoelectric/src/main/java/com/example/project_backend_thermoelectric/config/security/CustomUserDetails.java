package com.example.project_backend_thermoelectric.config.security;

import com.example.project_backend_thermoelectric.entity.User;
import com.example.project_backend_thermoelectric.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        var authorities = user.getRoles()
                .stream()
                .map(UserRole::getRole)
                .map(role ->
                        new SimpleGrantedAuthority(
                                "ROLE_" + role.getName().toUpperCase()
                        )
                )
                .toList();

        System.out.println(authorities);

        return authorities;
    }

    @Override
    public String getPassword() {

        return user.getPassword();
    }

    @Override
    public String getUsername() {

        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }
}