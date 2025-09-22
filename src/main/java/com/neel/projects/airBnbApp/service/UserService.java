package com.neel.projects.airBnbApp.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.neel.projects.airBnbApp.entity.User;

public interface UserService {
    User getUserById(Long id);
    UserDetails loadUserByUsername(String email);
}
