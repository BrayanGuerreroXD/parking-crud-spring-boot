package com.api.parking.controller;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.parking.dto.request.ParkingRequestDto;
import com.api.parking.dto.response.ParkingResponseDto;
import com.api.parking.service.ParkingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/parking")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> saveParking(@Valid @RequestBody ParkingRequestDto requestDto){
        try {
            ParkingResponseDto response = parkingService.saveParking(requestDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(Map.of("error", formatErrorMessage(e)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> updateParking(@Valid @RequestBody ParkingRequestDto requestDto){
        try {
            ParkingResponseDto response = parkingService.updateParking(requestDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(Map.of("error", formatErrorMessage(e))); 
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ParkingResponseDto> getParkingById(@PathVariable(name = "id") Long id){
        try {
            ParkingResponseDto response = parkingService.getParkingById(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteParking(@PathVariable(name = "id") Long id){
        try {
            Map<String, String> response = parkingService.deleteParking(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(Map.of("error", formatErrorMessage(e))); 
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ParkingResponseDto>> getParkingList(){
        try {
            List<ParkingResponseDto> response = parkingService.getParkingList();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/list/userId/{userId}")
    @PreAuthorize("hasAuthority('SOCIO')")
    public ResponseEntity<List<ParkingResponseDto>> getParkingListByUserId(@PathVariable(name = "userId") Long userId){
        try {
            List<ParkingResponseDto> response = parkingService.getParkingListByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
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