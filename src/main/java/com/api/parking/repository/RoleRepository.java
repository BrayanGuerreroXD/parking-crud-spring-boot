package com.api.parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.parking.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findFirstByName(String name);
}