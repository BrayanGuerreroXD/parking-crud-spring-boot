package com.api.parking.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EarningsResponseDto {
    private Double today;
    private Double week;
    private Double month;
    private Double year;
}
