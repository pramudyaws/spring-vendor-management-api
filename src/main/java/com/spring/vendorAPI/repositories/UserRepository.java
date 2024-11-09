package com.spring.vendorAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.vendorAPI.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
