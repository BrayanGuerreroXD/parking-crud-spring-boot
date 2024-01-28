package com.api.parking.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleResponseDto {
    private Long id;
    private String plate;
    private Long recordCount;   
}