package com.rest.paidparkingapi.exceptions;

public class VehicleTypeNotFoundException extends Exception {
    public VehicleTypeNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
