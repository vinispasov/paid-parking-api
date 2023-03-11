package com.rest.paidparkingapi.dtos.mappers;

import com.rest.paidparkingapi.dtos.ParkingDto;
import com.rest.paidparkingapi.entities.Parking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParkingMapper {
    Parking toEntity(ParkingDto parkingDto);
}
