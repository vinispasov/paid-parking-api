package com.rest.paidparkingapi.entities;

import com.rest.paidparkingapi.enums.VehicleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "vehicle_id")
    private Long vehicleId;
    @Column(name = "type")
    private VehicleType type;
    @Column(name = "enter_date_time")
    private LocalDateTime enterDateTime;

}
