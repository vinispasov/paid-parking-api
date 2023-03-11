package com.rest.paidparkingapi.integration;

import com.rest.paidparkingapi.controllers.ParkingController;
import com.rest.paidparkingapi.dtos.VehicleDto;
import com.rest.paidparkingapi.entities.Parking;
import com.rest.paidparkingapi.enums.VehicleType;
import com.rest.paidparkingapi.services.ParkingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ParkingTestIT {
    @Autowired
    private ParkingController parkingController;
    @Autowired
    private ParkingServiceImpl parkingService;

    private VehicleDto vehicleDto;
    private Parking parking;

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

        vehicleDto = new VehicleDto();
        vehicleDto.setType(VehicleType.CAR);

        parking = new Parking();
        parking.setParkingId(1L);
        parking.setCarsCurrentCount(0);
        parking.setBusesCurrentCount(0);
    }


    @Test
    public void when_enter_then_successful() {

        // act
        ResponseEntity<String> response = parkingController.enter(vehicleDto, parking.getParkingId());
        // assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), containsString("entered successfully!"));
    }

}
