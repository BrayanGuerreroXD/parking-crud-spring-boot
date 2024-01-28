package com.api.parking.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.parking.entity.Role;
import com.api.parking.repository.RoleRepository;
import com.api.parking.service.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role findByIdRole(Long idRole) {
        return roleRepository.findById(idRole)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));
    }

    @Override
    public Role findByName(String name) {
        Role role = roleRepository.findFirstByName(name);
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return role;
    }
}
