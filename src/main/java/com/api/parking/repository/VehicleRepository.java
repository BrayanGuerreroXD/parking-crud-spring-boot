package com.api.parking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.parking.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    Vehicle findByPlate(String plate);   

    @Query("SELECT v FROM Vehicle v WHERE v.plate LIKE %:plate%")
    List<Vehicle> findVehiclesByPlateContaining(@Param("plate") String plate);
}