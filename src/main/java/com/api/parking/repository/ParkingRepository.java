package com.api.parking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.parking.entity.Parking;

public interface ParkingRepository extends JpaRepository<Parking, Long> {
    List<Parking> findByUserId(Long userId);
}
