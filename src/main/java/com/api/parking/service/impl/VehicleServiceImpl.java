package com.api.parking.service.impl;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.parking.entity.Vehicle;
import com.api.parking.repository.VehicleRepository;
import com.api.parking.service.VehicleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    
    @Override
    @Transactional
    public Vehicle getOrCreateVehicle(String plate) {
        Vehicle vehicle = vehicleRepository.findByPlate(plate);
        if (vehicle == null) {
            vehicle = new Vehicle();
            vehicle.setPlate(plate);
            vehicle = vehicleRepository.save(vehicle);
        }
        return vehicle;
    }

    @Override
    public Vehicle getVehicleByPlate(String plate) {
        Vehicle vehicle = vehicleRepository.findByPlate(plate);
        if (vehicle == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return vehicle;
    }
}