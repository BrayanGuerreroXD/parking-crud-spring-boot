package com.api.parking.service;

import com.api.parking.entity.Vehicle;

public interface VehicleService {
    Vehicle getOrCreateVehicle(String plate);
    Vehicle getVehicleByPlate(String plate);
}