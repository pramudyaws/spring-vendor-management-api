package com.spring.vendorAPI.services.interfaces;

import java.util.Map;

import com.spring.vendorAPI.entities.User;

public interface AuthService {
    User registerUser(User user);
    Map<String, Object> login(String email, String password);
    boolean isEmailTaken(String email);
}
