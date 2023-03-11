package com.rest.paidparkingapi.unit.services;


import com.rest.paidparkingapi.dtos.VehicleDto;
import com.rest.paidparkingapi.dtos.mappers.VehicleMapper;
import com.rest.paidparkingapi.entities.Parking;
import com.rest.paidparkingapi.entities.Vehicle;
import com.rest.paidparkingapi.enums.VehicleType;
import com.rest.paidparkingapi.exceptions.ParkingException;
import com.rest.paidparkingapi.exceptions.ParkingNotAvailableException;
import com.rest.paidparkingapi.exceptions.VehicleNotFoundException;
import com.rest.paidparkingapi.exceptions.VehicleTypeNotFoundException;
import com.rest.paidparkingapi.repositories.ParkingRepository;
import com.rest.paidparkingapi.repositories.VehicleRepository;
import com.rest.paidparkingapi.services.ParkingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    @InjectMocks
    private ParkingServiceImpl parkingService;
    @Mock
    private ParkingRepository parkingRepository;
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private VehicleMapper vehicleMapper;
    private VehicleDto vehicleDto;
    private Vehicle vehicle;
    private Vehicle vehicleFromDatabase;
    private Parking parking;
    private LocalDateTime now;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(parkingService, "parkingCarsCapacity", 50);
        ReflectionTestUtils.setField(parkingService, "parkingBusesCapacity", 10);
        ReflectionTestUtils.setField(parkingService, "parkingCarsHourlyPrice", 1);
        ReflectionTestUtils.setField(parkingService, "parkingBusesHourlyPrice", 5);
        ReflectionTestUtils.setField(parkingService, "parkingCarsDailyPrice", 10);
        ReflectionTestUtils.setField(parkingService, "parkingBusesDailyPrice", 40);
        ReflectionTestUtils.setField(parkingService, "parkingCarsMaxHours", 10);
        ReflectionTestUtils.setField(parkingService, "parkingBusesMaxHours", 8);

        now = LocalDateTime.now();
        vehicleDto = new VehicleDto();
        vehicleDto.setType(VehicleType.CAR);
        vehicleDto.setEnterDateTime(now);

        vehicle = new Vehicle();
        vehicle.setType(vehicleDto.getType());
        vehicle.setEnterDateTime(vehicleDto.getEnterDateTime());

        parking = new Parking();
        parking.setParkingId(1L);
        parking.setCarsCurrentCount(0);
        parking.setBusesCurrentCount(0);

        vehicleFromDatabase = new Vehicle();
        vehicleFromDatabase.setVehicleId(123L);
        vehicleFromDatabase.setType(vehicle.getType());
        vehicleFromDatabase.setEnterDateTime(vehicle.getEnterDateTime());

    }


    @Test
    public void when_enter_then_successful() throws VehicleTypeNotFoundException, VehicleNotFoundException, ParkingNotAvailableException, ParkingException {

        // arrange
        when(parkingRepository.findById(parking.getParkingId())).thenReturn(Optional.of(parking));
        when(parkingRepository.save(parking)).thenReturn(parking);
        when(vehicleMapper.toEntity(vehicleDto)).thenReturn(vehicle);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicleFromDatabase);
        // act
        Long actualVehicleId = parkingService.enter(vehicleDto, parking.getParkingId());
        // assert
        assertEquals(vehicleFromDatabase.getVehicleId(), actualVehicleId);
    }

    @Test
    public void when_enter_withWrongParkingId_then_exception() {

        // arrange
        when(parkingRepository.findById(parking.getParkingId())).thenReturn(Optional.empty());
        // act
        Throwable exception = assertThrows(ParkingException.class, () -> parkingService.enter(vehicleDto, parking.getParkingId()));
        // assert
        assertEquals("Parking not found!", exception.getMessage());
    }

    @Test
    public void when_enter_andVehicleAlreadyEntered_then_exception() {

        // arrange
        vehicleDto.setVehicleId(vehicleFromDatabase.getVehicleId());
        when(parkingRepository.findById(parking.getParkingId())).thenReturn(Optional.of(parking));
        when(vehicleRepository.findById(vehicleFromDatabase.getVehicleId())).thenReturn(Optional.of(vehicleFromDatabase));
        when(vehicleMapper.toEntity(vehicleDto)).thenReturn(vehicleFromDatabase);

        // act
        Throwable exception = assertThrows(ParkingException.class, () -> parkingService.enter(vehicleDto, parking.getParkingId()));
        // assert
        assertEquals("Vehicle already entered!", exception.getMessage());
    }
}
