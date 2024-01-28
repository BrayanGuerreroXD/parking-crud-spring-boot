package com.api.parking.component;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.api.parking.dto.request.ParkingRequestDto;
import com.api.parking.dto.response.ParkingResponseDto;
import com.api.parking.entity.Parking;

@Component
public class ParkingConverter {

    public Parking convertParkingRequestToParkingEntity(ParkingRequestDto parkingRequestDto) {
        if (parkingRequestDto.getName() == null || parkingRequestDto.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The parking name is required");
        }

        if (parkingRequestDto.getHourlyCost() == null || parkingRequestDto.getHourlyCost() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The hourly cost must be equal to or greater than zero");
        }

        if (parkingRequestDto.getMaxParkingSpace() == null || parkingRequestDto.getMaxParkingSpace() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The max parking space must be greater than zero");
        }

        if (parkingRequestDto.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user with role 'socio' is required");
        }

        Parking parking = new Parking();
        parking.setName(parkingRequestDto.getName());
        parking.setHourlyCost(parkingRequestDto.getHourlyCost());
        parking.setMaxParkingSpace(parkingRequestDto.getMaxParkingSpace());
        return parking;
    }

    public ParkingResponseDto convertParkingEntityToParkingResponse(Parking parking) {
        return new ParkingResponseDto(parking.getId(), parking.getName(), parking.getHourlyCost(),
                parking.getMaxParkingSpace(), parking.getUser().getId());
    }
}