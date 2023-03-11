package com.rest.paidparkingapi.repositories;

import com.rest.paidparkingapi.entities.Sale;
import com.rest.paidparkingapi.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByParkingIdAndSaleDateBetweenAndVehicleType(Long parkingId, LocalDateTime from, LocalDateTime to, VehicleType vehicleType);

    List<Sale> findByParkingIdAndSaleDateBetween(Long parkingId, LocalDateTime from, LocalDateTime to);
}
