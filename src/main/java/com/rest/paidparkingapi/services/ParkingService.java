package com.rest.paidparkingapi.services;

import com.rest.paidparkingapi.dtos.ParkingDto;
import com.rest.paidparkingapi.dtos.VehicleDto;
import com.rest.paidparkingapi.entities.Parking;
import com.rest.paidparkingapi.exceptions.ParkingException;
import com.rest.paidparkingapi.exceptions.ParkingNotAvailableException;
import com.rest.paidparkingapi.exceptions.VehicleNotFoundException;
import com.rest.paidparkingapi.exceptions.VehicleTypeNotFoundException;
import lombok.NonNull;
import java.util.Optional;

public interface ParkingService {

    Long enter(@NonNull VehicleDto vehicle, Long parkingId) throws ParkingNotAvailableException, VehicleTypeNotFoundException, ParkingException, VehicleNotFoundException;

    void exit(Long parkingId, Long vehicleId) throws ParkingNotAvailableException, VehicleTypeNotFoundException, ParkingException, VehicleNotFoundException;

    Parking createParking(ParkingDto parkingDto);

    Optional<Parking> getAllVehiclesInParking(Long parkingId);
}
