package com.api.parking.service;

import java.util.List;
import java.util.Map;

import com.api.parking.dto.request.ParkingRequestDto;
import com.api.parking.dto.response.ParkingResponseDto;
import com.api.parking.entity.Parking;

public interface ParkingService {
    ParkingResponseDto saveParking(ParkingRequestDto requestDto);
    ParkingResponseDto updateParking(ParkingRequestDto requestDto);
    ParkingResponseDto getParkingById(Long id);
    Parking getParkingEntityById(Long id);
    List<ParkingResponseDto> getParkingList();
    List<ParkingResponseDto> getParkingListByUserId(Long userId);
    Map<String, String> deleteParking(Long id);
}