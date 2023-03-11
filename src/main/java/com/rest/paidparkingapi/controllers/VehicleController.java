package com.rest.paidparkingapi.controllers;

import com.rest.paidparkingapi.dtos.VehicleDto;
import com.rest.paidparkingapi.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/all")
    public ResponseEntity<List<VehicleDto>> findAll() {
        return ResponseEntity.ok().body(vehicleService.findAll());
    }
}
