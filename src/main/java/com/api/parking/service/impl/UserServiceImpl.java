package com.api.parking.service.impl;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.parking.dto.request.UserRequestDto;
import com.api.parking.dto.response.UserResponseDto;
import com.api.parking.entity.User;
import com.api.parking.repository.UserRepository;
import com.api.parking.service.RoleService;
import com.api.parking.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponseDto register(UserRequestDto requestDto) {
        this.validateUserRequestDto(requestDto);
        User user = new User();
        if(userRepository.findByEmail(requestDto.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRole(roleService.findByName("SOCIO"));
        User userSave = userRepository.save(user);
        return new UserResponseDto(userSave.getId(), userSave.getEmail(), requestDto.getPassword(), userSave.getRole());
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private void validateUserRequestDto(UserRequestDto requestDto) {
        if (requestDto.getEmail() == null || requestDto.getEmail().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The email is required");
        }
    
        if (!requestDto.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The email is not valid");
        }
    
        if (requestDto.getPassword() == null || requestDto.getPassword().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is required");
        }
    }
}
