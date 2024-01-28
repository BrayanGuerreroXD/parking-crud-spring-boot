package com.api.parking.dto.response;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ErrorResponseDto {
    private final String error;
    private final String message;
}