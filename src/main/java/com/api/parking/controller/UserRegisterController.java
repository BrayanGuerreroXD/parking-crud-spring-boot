package com.api.parking.controller;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.parking.dto.request.UserRequestDto;
import com.api.parking.dto.response.UserResponseDto;
import com.api.parking.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRegisterController {

    private final UserService userService;

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserRequestDto requestDto){
        try {
            UserResponseDto userResponseDto = userService.register(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(Map.of("error", formatErrorMessage(e)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private String formatErrorMessage(ResponseStatusException ex) {
        String rawErrorMessage = ex.getMessage();
        Pattern pattern = Pattern.compile("\"(.*?)\"");
        Matcher matcher = pattern.matcher(rawErrorMessage);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return rawErrorMessage;
        }
    }
}