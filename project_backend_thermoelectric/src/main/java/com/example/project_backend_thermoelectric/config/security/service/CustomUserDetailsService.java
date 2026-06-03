package com.example.project_backend_thermoelectric.config.security.service;

import com.example.project_backend_thermoelectric.entity.User;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IUserRepo;
import com.example.project_backend_thermoelectric.config.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("tài khoản không tồn tại !!")
                );

        return new CustomUserDetails(user);
    }
}