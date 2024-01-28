package com.api.parking.service;

import java.util.List;
import java.util.Map;

import com.api.parking.dto.request.ParkingRecordRequestDto;
import com.api.parking.dto.response.ParkingRecordResponseDto;
import com.api.parking.dto.response.VehicleResponseDto;

public interface ParkingRecordService {
    Map<String, Object> createParkingEntryRecord(ParkingRecordRequestDto parkingRecordRequestDto);
    Map<String, String> createParkingExitRecord(ParkingRecordRequestDto parkingRecordRequestDto);

    List<ParkingRecordResponseDto> getParkingRecordsWithExitDateNullByParkingId(Long parkingId);
    List<ParkingRecordResponseDto> getParkingRecordsFirstTimeWithExitDateNullByParkingId(Long parkingId);
    List<ParkingRecordResponseDto> getParkingRecordsByVehiclePlateMatches(String plate);
    List<VehicleResponseDto> getMostRegisteredVehiclesAtAllParking();
    List<VehicleResponseDto> getMostRegisteredVehiclesAtAllParkingByParkingId(Long parkingId);

    List<ParkingRecordResponseDto> getParkingRecordsWithExitDateNullByParkingIdAndUserId(Long parkingId, Long userId);
    List<ParkingRecordResponseDto> getParkingRecordsFirstTimeWithExitDateNullByParkingIdAndUserId(Long parkingId, Long userId);
    List<ParkingRecordResponseDto> getParkingRecordsByUserIdAndVehiclePlateMatches(String plate, Long userId);
    List<VehicleResponseDto> getMostRegisteredVehiclesAtAllParkingByUserId(Long userId);
    List<VehicleResponseDto> getMostRegisteredVehiclesAtAllParkingByParkingIdAndUserId(Long parkingId, Long userId);
}