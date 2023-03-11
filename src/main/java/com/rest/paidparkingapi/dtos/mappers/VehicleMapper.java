package com.rest.paidparkingapi.dtos.mappers;

import com.rest.paidparkingapi.dtos.VehicleDto;
import com.rest.paidparkingapi.entities.Vehicle;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    Vehicle toEntity(VehicleDto vehicleDto);

    VehicleDto toDto(Vehicle vehicle);

    List<VehicleDto> toDtoList(List<Vehicle> vehicles);
}
