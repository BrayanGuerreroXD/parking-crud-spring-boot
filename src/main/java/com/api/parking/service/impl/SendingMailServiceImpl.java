package com.api.parking.service.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.api.parking.dto.request.MailMessageRequestDto;
import com.api.parking.dto.request.SendMessageRequestDto;
import com.api.parking.dto.response.MailMessageResponseDto;
import com.api.parking.entity.Parking;
import com.api.parking.service.ParkingService;
import com.api.parking.service.SendingMailService;
import com.api.parking.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SendingMailServiceImpl implements SendingMailService {

    @Value("${url.api.mail}")
    private String urlApiMail;

    private final RestTemplate restTemplate;
    private final UserService userService;
    private final ParkingService parkingService;

    @Override
    public MailMessageResponseDto sendMail(MailMessageRequestDto mailMessageRequestDto) {
        this.validateMailMessageRequestDto(mailMessageRequestDto);

        if(userService.findByEmail(mailMessageRequestDto.getEmail()) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not found");
        }

        Parking parking = parkingService.getParkingEntityById(mailMessageRequestDto.getParkingId());

        // Create the request to send the mail to the API
        SendMessageRequestDto sendMessageRequestDto = new SendMessageRequestDto(
            mailMessageRequestDto.getEmail(),
            mailMessageRequestDto.getPlate(),
            mailMessageRequestDto.getMessage(),
            parking.getName()
        );
        
        MailMessageResponseDto response = new MailMessageResponseDto();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<SendMessageRequestDto> requestEntity = new HttpEntity<>(sendMessageRequestDto, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
            urlApiMail, HttpMethod.POST, requestEntity, String.class);

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            response.setMessage("Mail sent successfully");
            return response;
        } else {
            throw new RuntimeException("Error sending mail");
        }
    }

    private void validateMailMessageRequestDto(MailMessageRequestDto requestDto) {
    if (requestDto.getEmail() == null || requestDto.getEmail().trim().isEmpty()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The email is required");
    }

    if (!requestDto.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$")) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The email is not valid");
    }

    if (requestDto.getPlate() == null || requestDto.getPlate().length() != 6) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The plate must have exactly 6 characters");
    }

    if (requestDto.getMessage() == null || requestDto.getMessage().trim().isEmpty()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The message is required");
    }

    if (requestDto.getParkingId() == null || requestDto.getParkingId() <= 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The parkingId is required and must be greater than 0");
    }
}
}