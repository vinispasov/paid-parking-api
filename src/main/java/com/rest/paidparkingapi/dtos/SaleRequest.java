package com.rest.paidparkingapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequest {
    private Long parkingId;
    private LocalDateTime from;
    private LocalDateTime to;
    private Integer year;
    private Integer month;
}
