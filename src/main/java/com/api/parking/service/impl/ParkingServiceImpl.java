package com.api.parking.service.impl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.parking.component.ParkingConverter;
import com.api.parking.dto.request.ParkingRequestDto;
import com.api.parking.dto.response.ParkingResponseDto;
import com.api.parking.entity.Parking;
import com.api.parking.entity.User;
import com.api.parking.repository.ParkingRepository;
import com.api.parking.service.ParkingService;
import com.api.parking.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {
    private final ParkingRepository parkingRepository;
    private final ParkingConverter parkingConverter;
    private final UserService userService;

    @Override
    @Transactional
    public ParkingResponseDto saveParking(ParkingRequestDto requestDto) {
        Parking parking = parkingConverter.convertParkingRequestToParkingEntity(requestDto);
        User user = userService.findById(requestDto.getUserId());

        // If the user is ADMIN then it throws an exception because only the user with role 'SOCIO' can be assigned to the parking
        if (user.getRole().getName().equals("ADMIN")) {
            throw new RuntimeException("User isn't a 'SOCIO'");
        }

        parking.setUser(user);
        Parking parkingSave = parkingRepository.save(parking);

        return parkingConverter.convertParkingEntityToParkingResponse(parkingSave);
    }

    @Override
    @Transactional
    public ParkingResponseDto updateParking(ParkingRequestDto requestDto) {
        if(requestDto.getId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parking id is required");
        Parking parking = parkingRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("Parking not found"));
        parking.setName(requestDto.getName());
        parking.setHourlyCost(requestDto.getHourlyCost());
        parking.setMaxParkingSpace(requestDto.getMaxParkingSpace());

        // If the user is different from the one that is assigned to the parking, then it is updated
        if(parking.getUser().getId() != requestDto.getUserId()){
            User user = userService.findById(requestDto.getUserId());
            // If the user is ADMIN then it throws an exception because only the user with role 'SOCIO' can be assigned to the parking
            if (user.getRole().getName().equals("ADMIN")) {
                throw new RuntimeException("User isn't a 'SOCIO");
            }
            parking.setUser(user);
        }

        Parking parkingSave = parkingRepository.save(parking);

        return parkingConverter.convertParkingEntityToParkingResponse(parkingSave);
    }

    @Override
    public ParkingResponseDto getParkingById(Long id) {
        Parking parking = parkingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking not found"));
        return parkingConverter.convertParkingEntityToParkingResponse(parking);
    }

    @Override
    public Parking getParkingEntityById(Long id) {
        return parkingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Parking not found"));
    }

    @Override
    @Transactional
    public Map<String, String> deleteParking(Long id) {
        Parking parking = parkingRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parking not found"));
        parkingRepository.delete(parking);
        return Map.of("message", "Parking deleted successfully");
    }

    @Override
    public List<ParkingResponseDto> getParkingList() {
        return parkingRepository.findAll().stream().map(parking -> parkingConverter.convertParkingEntityToParkingResponse(parking)).toList();
    }

    @Override
    public List<ParkingResponseDto> getParkingListByUserId(Long userId) {
        return parkingRepository.findByUserId(userId).stream().map(parking -> parkingConverter.convertParkingEntityToParkingResponse(parking)).toList();
    }
}