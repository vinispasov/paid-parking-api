package com.rest.paidparkingapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Parking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "parking_id")
    private Long parkingId;
    @Column(name = "cars_current_count")
    private Integer carsCurrentCount;
    @Column(name = "buses_current_count")
    private Integer busesCurrentCount;
    @OneToMany
    private List<Vehicle> vehicles;
}
