package com.rest.paidparkingapi.exceptions;

public class VehicleNotFoundException extends Exception {
    public VehicleNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
