package com.api.parking.controller;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.parking.dto.response.EarningsResponseDto;
import com.api.parking.service.HistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;

    @GetMapping("/earnings/parkingId/{parkingId}/userId/{userId}")
    @PreAuthorize("hasAuthority('SOCIO')")
    public ResponseEntity<Object> getEarnings(@PathVariable Long parkingId, @PathVariable Long userId) {
        try {
            EarningsResponseDto response = historyService.getEarnings(parkingId, userId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(Map.of("message", formatErrorMessage(e)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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