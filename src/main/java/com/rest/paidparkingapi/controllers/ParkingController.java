package com.rest.paidparkingapi.controllers;

import com.rest.paidparkingapi.dtos.ParkingDto;
import com.rest.paidparkingapi.dtos.VehicleDto;
import com.rest.paidparkingapi.entities.Parking;
import com.rest.paidparkingapi.exceptions.ParkingException;
import com.rest.paidparkingapi.exceptions.ParkingNotAvailableException;
import com.rest.paidparkingapi.exceptions.VehicleNotFoundException;
import com.rest.paidparkingapi.exceptions.VehicleTypeNotFoundException;
import com.rest.paidparkingapi.services.ParkingService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @PostMapping("/create")
    public ResponseEntity<Parking> createParking(@RequestBody @NonNull ParkingDto parkingDto) {
        return ResponseEntity.ok().body(parkingService.createParking(parkingDto));
    }

    @PostMapping("/enter/{parkingId}")
    public ResponseEntity<String> enter(@RequestBody @NonNull VehicleDto vehicleDto, @PathVariable("parkingId") Long parkingId) {
        try {
            Long vehicleId = parkingService.enter(vehicleDto, parkingId);
            return ResponseEntity.ok().body(String.format("Vehicle with id %s entered successfully!", vehicleId));
        } catch (ParkingNotAvailableException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        } catch (VehicleTypeNotFoundException | ParkingException | VehicleNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/exit/{parkingId}/{vehicleId}")
    public ResponseEntity<String> exit(@PathVariable("parkingId") Long parkingId, @PathVariable("vehicleId") Long vehicleId) {
        try {
            parkingService.exit(parkingId, vehicleId);
            return ResponseEntity.ok().body(String.format("Vehicle with id %s exit successfully!", vehicleId));
        } catch (ParkingNotAvailableException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        } catch (VehicleTypeNotFoundException | ParkingException | VehicleNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{parkingId}")
    public ResponseEntity<Optional<Parking>> getAllVehiclesInParking(@PathVariable("parkingId") Long parkingId) {
        return ResponseEntity.ok().body(parkingService.getAllVehiclesInParking(parkingId));
    }
}
