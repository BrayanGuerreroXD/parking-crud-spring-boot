package com.api.parking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ParkingResponseDto {
    private Long id;
    private String name;
    private Double hourlyCost;
    private Integer maxParkingSpace;
    private Long userId;
}
