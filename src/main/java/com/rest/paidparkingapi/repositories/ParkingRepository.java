package com.rest.paidparkingapi.repositories;

import com.rest.paidparkingapi.entities.Parking;
import com.rest.paidparkingapi.entities.Sale;
import com.rest.paidparkingapi.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ParkingRepository extends JpaRepository<Parking, Long> {
    Optional<Parking> findParkingByParkingIdAndVehiclesIsContaining(Long parkingId, Vehicle vehicle);
    //Optional<Parking> findParkingByParkingIdAnd
}
