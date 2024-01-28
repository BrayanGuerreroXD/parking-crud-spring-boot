package com.api.parking.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingRequestDto {
    private Long id;
    private String name;
    private Double hourlyCost;
    private Integer maxParkingSpace;
    private Long userId;
}
