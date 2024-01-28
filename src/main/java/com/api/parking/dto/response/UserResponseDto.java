package com.api.parking.dto.response;

import com.api.parking.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String password;
    private Role role;
}