package com.rest.paidparkingapi.dtos;

import com.rest.paidparkingapi.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto {
    private Long vehicleId;
    private VehicleType type;
    private LocalDateTime enterDateTime;
}
