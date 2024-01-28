package com.api.parking.controller;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.parking.dto.request.ParkingRecordRequestDto;
import com.api.parking.dto.response.ParkingRecordResponseDto;
import com.api.parking.dto.response.VehicleResponseDto;
import com.api.parking.service.ParkingRecordService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/parkingRecord")
@RequiredArgsConstructor
public class ParkingRecordController {

    private final ParkingRecordService parkingRecordService;

    @PostMapping("/entry")
    @PreAuthorize("hasAuthority('SOCIO')")
    public ResponseEntity<Map<String, Object>> createParkingEntryRecord(
            @Valid @RequestBody ParkingRecordRequestDto parkingRecordRequestDto) {
        try {
            Map<String, Object> response = parkingRecordService.createParkingEntryRecord(parkingRecordRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(Map.of("message", formatErrorMessage(e)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An internal error occurred."));
        }
    }

    @PostMapping("/exit")
    @PreAuthorize("hasAuthority('SOCIO')")
    public ResponseEntity<Map<String, String>> createParkingExitRecord(
            @Valid @RequestBody ParkingRecordRequestDto parkingRecordRequestDto) {
        try {
            Map<String, String> response = parkingRecordService.createParkingExitRecord(parkingRecordRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(Map.of("message", formatErrorMessage(e)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An internal error occurred."));
        }
    }

    @GetMapping("/parkedVehicles/parkingId/{parkingId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ParkingRecordResponseDto>> getParkingRecordsWithExitDateNullByParkingId(
            @PathVariable(name = "parkingId") Long parkingId) {
        try {
            List<ParkingRecordResponseDto> response = parkingRecordService
                    .getParkingRecordsWithExitDateNullByParkingId(parkingId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/parkedVehicles/parkingId/{parkingId}/userId/{userId}")
    @PreAuthorize("hasAuthority('SOCIO')")
    public ResponseEntity<List<ParkingRecordResponseDto>> getParkingRecordsWithExitDateNullByParkingIdAndUserId(
            @PathVariable(name = "parkingId") Long parkingId, @PathVariable(name = "userId") Long userId) {
        try {
            List<ParkingRecordResponseDto> response = parkingRecordService
                    .getParkingRecordsWithExitDateNullByParkingIdAndUserId(parkingId, userId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtain vehicles parked for the first time in that parking lot.

    @GetMapping("/parkedVehicles/firstTime/parkingId/{parkingId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ParkingRecordResponseDto>> getParkingRecordsFirstTimeWithExitDateNullByParkingId(
            @PathVariable(name = "parkingId") Long parkingId) {
        try {
            List<ParkingRecordResponseDto> response = parkingRecordService
                    .getParkingRecordsFirstTimeWithExitDateNullByParkingId(parkingId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/parkedVehicles/firstTime/parkingId/{parkingId}/userId/{userId}")
    @PreAuthorize("hasAuthority('SOCIO')")
    public ResponseEntity<List<ParkingRecordResponseDto>> getParkingRecordsFirstTimeWithExitDateNullByParkingIdAndUserId(
            @PathVariable(name = "parkingId") Long parkingId, @PathVariable(name = "userId") Long userId) {
        try {
            List<ParkingRecordResponseDto> response = parkingRecordService
                    .getParkingRecordsFirstTimeWithExitDateNullByParkingIdAndUserId(parkingId, userId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Search for parked vehicles by license plate match

    @GetMapping("/parkedVehicles/matches/plate/{plate}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ParkingRecordResponseDto>> getParkingRecordsByVehiclePlateMatches(
            @PathVariable(name = "plate") String plate) {
        try {
            List<ParkingRecordResponseDto> response = parkingRecordService
                    .getParkingRecordsByVehiclePlateMatches(plate);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/parkedVehicles/matches/plate/{plate}/userId/{userId}")
    @PreAuthorize("hasAuthority('SOCIO')")
    public ResponseEntity<List<ParkingRecordResponseDto>> getParkingRecordsByUserIdAndVehiclePlateMatches(
            @PathVariable(name = "plate") String plate, @PathVariable(name = "userId") Long userId) {
        try {
            List<ParkingRecordResponseDto> response = parkingRecordService
                    .getParkingRecordsByUserIdAndVehiclePlateMatches(plate, userId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Find the top 10 vehicles that have been registered the most times in all parking lots and how many times they have been registered.

    @GetMapping("/parkedVehicles/mostRegistered")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<VehicleResponseDto>> getMostRegisteredVehiclesAtAllParking() {
        try {
            List<VehicleResponseDto> response = parkingRecordService.getMostRegisteredVehiclesAtAllParking();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/parkedVehicles/mostRegistered/userId/{userId}")
    @PreAuthorize("hasAuthority('SOCIO')")
    public ResponseEntity<List<VehicleResponseDto>> getMostRegisteredVehiclesAtAllParkingByUserId(@PathVariable(name = "userId") Long userId) {
        try {
            List<VehicleResponseDto> response = parkingRecordService.getMostRegisteredVehiclesAtAllParkingByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Find the 10 vehicles that have been registered the most times in a specific parking lot and how many times they have been registered.

    @GetMapping("/parkedVehicles/mostRegistered/parkingId/{parkingId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<VehicleResponseDto>> getMostRegisteredVehiclesAtAllParkingByParkingId(
            @PathVariable(name = "parkingId") Long parkingId) {
        try {
            List<VehicleResponseDto> response = parkingRecordService
                    .getMostRegisteredVehiclesAtAllParkingByParkingId(parkingId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/parkedVehicles/mostRegistered/parkingId/{parkingId}/userId/{userId}")
    @PreAuthorize("hasAuthority('SOCIO')")
    public ResponseEntity<List<VehicleResponseDto>> getMostRegisteredVehiclesAtAllParkingByParkingIdAndUserId(
            @PathVariable(name = "parkingId") Long parkingId, @PathVariable(name = "userId") Long userId) {
        try {
            List<VehicleResponseDto> response = parkingRecordService
                    .getMostRegisteredVehiclesAtAllParkingByParkingIdAndUserId(parkingId, userId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
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
