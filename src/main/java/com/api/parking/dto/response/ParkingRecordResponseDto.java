package com.api.parking.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingRecordResponseDto {
    private Long id;
    private String plate;
    private LocalDateTime entryDate;
}