package com.api.parking.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailMessageRequestDto {
    private String email;
    private String plate;
    private String message;
    private Long parkingId;
}