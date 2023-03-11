package com.rest.paidparkingapi.exceptions;

public class ParkingNotAvailableException extends Exception {
    public ParkingNotAvailableException(String errorMessage) {
        super(errorMessage);
    }
}
