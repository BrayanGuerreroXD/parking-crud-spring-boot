package com.api.parking.service;

import com.api.parking.dto.request.UserRequestDto;
import com.api.parking.dto.response.UserResponseDto;
import com.api.parking.entity.User;

public interface UserService {
    UserResponseDto register(UserRequestDto requestDto);
    User findById(Long userId);
    User findByEmail(String email);
}