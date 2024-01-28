package com.api.parking.controller;

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

import com.api.parking.dto.request.MailMessageRequestDto;
import com.api.parking.dto.response.MailMessageResponseDto;
import com.api.parking.service.SendingMailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sendingMail")
@RequiredArgsConstructor
public class SendingMailController {
    private final SendingMailService sendingMailService;

    @PostMapping("/send")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MailMessageResponseDto> sendMail(@Valid @RequestBody MailMessageRequestDto requestDto){
        MailMessageResponseDto response = new MailMessageResponseDto();
        try {
            response = sendingMailService.sendMail(requestDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ResponseStatusException e) {
            response.setMessage(formatErrorMessage(e));
            return ResponseEntity.status(e.getStatus()).body(response);
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