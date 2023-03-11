package com.rest.paidparkingapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingDto {
    private Long parkingId;
    private Integer carsCurrentCount;
    private Integer busesCurrentCount;
}
