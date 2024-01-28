package com.api.parking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SendMessageRequestDto {
    private String email;
    private String plate;
    private String message;
    private String parkingName;
}
