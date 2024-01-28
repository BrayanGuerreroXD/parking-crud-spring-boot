package com.api.parking.service;

import com.api.parking.entity.Role;

public interface RoleService {
    Role findByIdRole(Long idRole);
    Role findByName(String name);
}