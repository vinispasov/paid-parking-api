package com.rest.paidparkingapi.services;

import com.rest.paidparkingapi.dtos.VehicleDto;
import com.rest.paidparkingapi.dtos.mappers.VehicleMapper;
import com.rest.paidparkingapi.entities.Vehicle;
import com.rest.paidparkingapi.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private VehicleMapper vehicleMapper;

    public VehicleServiceImpl() {
    }

    public List<VehicleDto> findAll() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicleMapper.toDtoList(vehicles);
    }
}
