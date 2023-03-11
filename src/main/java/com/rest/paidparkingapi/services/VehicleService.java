package com.rest.paidparkingapi.services;

import com.rest.paidparkingapi.dtos.VehicleDto;
import java.util.List;

public interface VehicleService {
    List<VehicleDto> findAll();
}
