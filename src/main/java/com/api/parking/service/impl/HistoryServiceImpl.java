package com.api.parking.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.parking.dto.response.EarningsResponseDto;
import com.api.parking.entity.History;
import com.api.parking.entity.Parking;
import com.api.parking.repository.HistoryRepository;
import com.api.parking.service.HistoryService;
import com.api.parking.service.ParkingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final ParkingService parkingService;
    
    @Override
    @Transactional
    public void save(History history) {
        historyRepository.save(history);
    }

    @Override
    public EarningsResponseDto getEarnings(Long parkingId, Long userId) {
        // Validate that the parking belongs to the user
        Parking parking = parkingService.getParkingEntityById(parkingId);
        if(parking.getUser().getId() != userId){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The parking doesn't belong to the user");
        }
        // Get earnings for today, this week, this month and this year
        EarningsResponseDto response = new EarningsResponseDto();

        Double today = getEarningsForToday(parkingId);
        Double week = getEarningsForThisWeek(parkingId);
        Double month = getEarningsForThisMonth(parkingId);
        Double year = getEarningsForThisYear(parkingId);

        response.setToday(today != null ? today : 0.0);
        response.setWeek(week != null ? week : 0.0);
        response.setMonth(month != null ? month : 0.0);
        response.setYear(year != null ? year : 0.0);
        
        return response;
    }

    private Double getEarningsForToday(Long parkingId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        // LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        return historyRepository.calculateEarningsByDateRange(parkingId, startOfDay, now);
    }

    private Double getEarningsForThisWeek(Long parkingId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).withHour(0).withMinute(0).withSecond(0).withNano(0);
        // LocalDateTime endOfWeek = startOfWeek.plusDays(6).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        return historyRepository.calculateEarningsByDateRange(parkingId, startOfWeek, now);
    }

    private Double getEarningsForThisMonth(Long parkingId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0).withNano(0);
        // LocalDateTime endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        return historyRepository.calculateEarningsByDateRange(parkingId, startOfMonth, now);
    }

    private Double getEarningsForThisYear(Long parkingId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfYear = LocalDateTime.of(now.getYear(), 1, 1, 0, 0);
        // LocalDateTime endOfYear = LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59, 999999999);
        return historyRepository.calculateEarningsByDateRange(parkingId, startOfYear, now);
    }
}