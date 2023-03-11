package com.rest.paidparkingapi.entities;

import com.rest.paidparkingapi.enums.VehicleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sale_id")
    private Long saleId;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "sale_date")
    private LocalDateTime saleDate;
    @Column(name = "vehicle_type")
    private VehicleType vehicleType;
    @Column(name = "parkingId")
    private Long parkingId;
}
