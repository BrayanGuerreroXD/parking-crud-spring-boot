package com.api.parking.service;


import com.api.parking.dto.request.MailMessageRequestDto;
import com.api.parking.dto.response.MailMessageResponseDto;

public interface SendingMailService {
    MailMessageResponseDto sendMail(MailMessageRequestDto mailMessageRequestDto);
}   