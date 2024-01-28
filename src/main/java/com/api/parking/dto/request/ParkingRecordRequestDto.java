package com.api.parking.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingRecordRequestDto {
    private String plate;
    private Long parkingId;
}