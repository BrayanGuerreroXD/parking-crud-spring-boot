package com.api.parking.service;

import com.api.parking.dto.response.EarningsResponseDto;
import com.api.parking.entity.History;

public interface HistoryService {
    void save(History history);
    EarningsResponseDto getEarnings(Long parkingId, Long userId);
}