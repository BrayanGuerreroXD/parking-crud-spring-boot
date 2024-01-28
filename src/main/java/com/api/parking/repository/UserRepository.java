package com.api.parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.parking.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}