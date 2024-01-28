package com.api.parking.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.parking.dto.request.ParkingRecordRequestDto;
import com.api.parking.dto.response.ParkingRecordResponseDto;
import com.api.parking.dto.response.VehicleResponseDto;
import com.api.parking.entity.History;
import com.api.parking.entity.Parking;
import com.api.parking.entity.ParkingRecord;
import com.api.parking.entity.Vehicle;
import com.api.parking.repository.ParkingRecordRepository;
import com.api.parking.service.HistoryService;
import com.api.parking.service.ParkingRecordService;
import com.api.parking.service.ParkingService;
import com.api.parking.service.VehicleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkingRecordServiceImpl implements ParkingRecordService {
    private final ParkingRecordRepository parkingRecordRepository;
    private final ParkingService parkingService;
    private final VehicleService vehicleService;
    private final HistoryService historyService;

    @Override
    @Transactional
    public Map<String, Object> createParkingEntryRecord(ParkingRecordRequestDto parkingRecordRequestDto) {
        this.validateParkingRecordRequestDto(parkingRecordRequestDto);
        Parking parking = parkingService.getParkingEntityById(parkingRecordRequestDto.getParkingId());
        
        Integer parkingCapacity = parking.getMaxParkingSpace();
        Integer parkingRecordsCount = parkingRecordRepository.countByParkingIdAndExitDateIsNull(parking.getId());

        if(parkingCapacity > 0 &&  parkingRecordsCount == parkingCapacity){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parking is full");
        }

        Vehicle vehicle = vehicleService.getOrCreateVehicle(parkingRecordRequestDto.getPlate());
        List<ParkingRecord> parkingRecords = parkingRecordRepository
            .findByVehicleIdAndExitDateIsNull(vehicle.getId());

        if(parkingRecords.size() > 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to Register Entry, the plate already exists in this or another parking lot.");
        }

        ParkingRecord parkingRecord = new ParkingRecord();
        parkingRecord.setVehicle(vehicle);
        parkingRecord.setParking(parking);
        parkingRecord.setEntryDate(LocalDateTime.now());
        parkingRecord = parkingRecordRepository.save(parkingRecord);

        return Map.of("id", parkingRecord.getId());
    }

    @Override
    public Map<String, String> createParkingExitRecord(ParkingRecordRequestDto parkingRecordRequestDto) {
        this.validateParkingRecordRequestDto(parkingRecordRequestDto);
        Vehicle vehicle = vehicleService.getVehicleByPlate(parkingRecordRequestDto.getPlate());
        Parking parking = parkingService.getParkingEntityById(parkingRecordRequestDto.getParkingId());

        ParkingRecord parkingRecord = parkingRecordRepository
            .findFirstByVehicleIdAndParkingIdAndExitDateIsNull(vehicle.getId(), parking.getId());

        if(parkingRecord == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't Register Exit, no license plate in the parking lot.");
        }

        parkingRecord.setExitDate(LocalDateTime.now());
        
        parkingRecord = parkingRecordRepository.save(parkingRecord);
        this.saveHistory(parkingRecord);

        return Map.of("Message", "Output registered");
    }

    // ADMIN

    @Override
    public List<ParkingRecordResponseDto> getParkingRecordsWithExitDateNullByParkingId(Long parkingId) {
        return parkingRecordRepository.findByParkingIdAndExitDateIsNull(parkingId).stream()
            .map(parkingRecord -> {
                ParkingRecordResponseDto parkingRecordResponseDto = new ParkingRecordResponseDto();
                parkingRecordResponseDto.setId(parkingRecord.getId());
                parkingRecordResponseDto.setPlate(parkingRecord.getVehicle().getPlate());
                parkingRecordResponseDto.setEntryDate(parkingRecord.getEntryDate());
                return parkingRecordResponseDto;
            })
            .toList();
    }

    @Override
    public List<ParkingRecordResponseDto> getParkingRecordsFirstTimeWithExitDateNullByParkingId(Long parkingId) {
        return parkingRecordRepository.getParkingRecordsFirstTimeWithExitDateNullByParkingId(parkingId).stream()
            .map(parkingRecord -> {
                ParkingRecordResponseDto parkingRecordResponseDto = new ParkingRecordResponseDto();
                parkingRecordResponseDto.setId(parkingRecord.getId());
                parkingRecordResponseDto.setPlate(parkingRecord.getVehicle().getPlate());
                parkingRecordResponseDto.setEntryDate(parkingRecord.getEntryDate());
                return parkingRecordResponseDto;
            })
            .toList();
    }

    @Override
    public List<ParkingRecordResponseDto> getParkingRecordsByVehiclePlateMatches(String plate) {
        return parkingRecordRepository.getParkingRecordsByVehiclePlateMatches(plate).stream()
            .map(parkingRecord -> {
                ParkingRecordResponseDto parkingRecordResponseDto = new ParkingRecordResponseDto();
                parkingRecordResponseDto.setId(parkingRecord.getId());
                parkingRecordResponseDto.setPlate(parkingRecord.getVehicle().getPlate());
                parkingRecordResponseDto.setEntryDate(parkingRecord.getEntryDate());
                return parkingRecordResponseDto;
            })
            .toList();
    }

    @Override
    public List<VehicleResponseDto> getMostRegisteredVehiclesAtAllParking() {
        return parkingRecordRepository.getMostRegisteredVehiclesAtAllParking().stream()
            .map(vehicle -> {
                VehicleResponseDto vehicleResponseDto = new VehicleResponseDto();
                vehicleResponseDto.setId(Long.parseLong(vehicle[0].toString()));
                vehicleResponseDto.setPlate(vehicle[1].toString());
                vehicleResponseDto.setRecordCount(Long.parseLong(vehicle[2].toString()));
                return vehicleResponseDto;
            })
            .toList();
    }
    
    @Override
    public List<VehicleResponseDto> getMostRegisteredVehiclesAtAllParkingByParkingId(Long parkingId) {
        return parkingRecordRepository.getMostRegisteredVehiclesAtAllParkingByParkingId(parkingId).stream()
            .map(vehicle -> {
                VehicleResponseDto vehicleResponseDto = new VehicleResponseDto();
                vehicleResponseDto.setId(Long.parseLong(vehicle[0].toString()));
                vehicleResponseDto.setPlate(vehicle[1].toString());
                vehicleResponseDto.setRecordCount(Long.parseLong(vehicle[2].toString()));
                return vehicleResponseDto;
            })
            .toList();
    }

    // SOCIO

    @Override
    public List<ParkingRecordResponseDto> getParkingRecordsWithExitDateNullByParkingIdAndUserId(Long parkingId, Long userId) {
        return parkingRecordRepository.findByParkingIdAndParkingUserIdAndExitDateIsNull(parkingId, userId).stream()
            .map(parkingRecord -> {
                ParkingRecordResponseDto parkingRecordResponseDto = new ParkingRecordResponseDto();
                parkingRecordResponseDto.setId(parkingRecord.getId());
                parkingRecordResponseDto.setPlate(parkingRecord.getVehicle().getPlate());
                parkingRecordResponseDto.setEntryDate(parkingRecord.getEntryDate());
                return parkingRecordResponseDto;
            })
            .toList();
    }

    @Override
    public List<ParkingRecordResponseDto> getParkingRecordsFirstTimeWithExitDateNullByParkingIdAndUserId(Long parkingId, Long userId) {
        return parkingRecordRepository.getParkingRecordsFirstTimeWithExitDateNullByParkingIdAndUserId(parkingId, userId).stream()
            .map(parkingRecord -> {
                ParkingRecordResponseDto parkingRecordResponseDto = new ParkingRecordResponseDto();
                parkingRecordResponseDto.setId(parkingRecord.getId());
                parkingRecordResponseDto.setPlate(parkingRecord.getVehicle().getPlate());
                parkingRecordResponseDto.setEntryDate(parkingRecord.getEntryDate());
                return parkingRecordResponseDto;
            })
            .toList();
    }

    @Override
    public List<ParkingRecordResponseDto> getParkingRecordsByUserIdAndVehiclePlateMatches(String plate, Long userId) {
        return parkingRecordRepository.getParkingRecordsByUserIdAndVehiclePlateMatches(userId, plate).stream()
            .map(parkingRecord -> {
                ParkingRecordResponseDto parkingRecordResponseDto = new ParkingRecordResponseDto();
                parkingRecordResponseDto.setId(parkingRecord.getId());
                parkingRecordResponseDto.setPlate(parkingRecord.getVehicle().getPlate());
                parkingRecordResponseDto.setEntryDate(parkingRecord.getEntryDate());
                return parkingRecordResponseDto;
            })
            .toList();
    }

    @Override
    public List<VehicleResponseDto> getMostRegisteredVehiclesAtAllParkingByUserId(Long userId) {
        return parkingRecordRepository.getMostRegisteredVehiclesAtAllParkingByUserId(userId).stream()
            .map(vehicle -> {
                VehicleResponseDto vehicleResponseDto = new VehicleResponseDto();
                vehicleResponseDto.setId(Long.parseLong(vehicle[0].toString()));
                vehicleResponseDto.setPlate(vehicle[1].toString());
                vehicleResponseDto.setRecordCount(Long.parseLong(vehicle[2].toString()));
                return vehicleResponseDto;
            })
            .toList();
    }

    @Override
    public List<VehicleResponseDto> getMostRegisteredVehiclesAtAllParkingByParkingIdAndUserId(Long parkingId, Long userId) {
        return parkingRecordRepository.getMostRegisteredVehiclesAtAllParkingByParkingIdAndUserId(parkingId, userId).stream()
            .map(vehicle -> {
                VehicleResponseDto vehicleResponseDto = new VehicleResponseDto();
                vehicleResponseDto.setId(Long.parseLong(vehicle[0].toString()));
                vehicleResponseDto.setPlate(vehicle[1].toString());
                vehicleResponseDto.setRecordCount(Long.parseLong(vehicle[2].toString()));
                return vehicleResponseDto;
            })
            .toList();
    }

    private void saveHistory(ParkingRecord parkingRecord) {
        History history = new History();
        history.setParkingRecord(parkingRecord);
        long hours = calculateHoursDifference(parkingRecord.getEntryDate(), parkingRecord.getExitDate());
        Double price = hours * parkingRecord.getParking().getHourlyCost();
        history.setTotalCost(price);
        historyService.save(history);
    }

    private static long calculateHoursDifference(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        Duration duration = Duration.between(dateTime1, dateTime2);
        return duration.toHours();
    }

    private void validateParkingRecordRequestDto(ParkingRecordRequestDto requestDto) {
        if (requestDto.getPlate() == null || requestDto.getPlate().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The plate is required");
        }

        if (requestDto.getPlate().length() != 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The plate must be exactly 6 characters");
        }

        if (!requestDto.getPlate().matches("^[a-zA-Z0-9]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The plate must be alphanumeric, no special characters or letter 'ñ'");
        }

        if (requestDto.getParkingId() == null || requestDto.getParkingId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The parkingId is required and must be greater than 0");
        }
    }
}