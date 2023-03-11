package com.rest.paidparkingapi.exceptions;

public class ParkingException extends Exception{
    public ParkingException(String errorMessage) {
        super(errorMessage);
    }
}
