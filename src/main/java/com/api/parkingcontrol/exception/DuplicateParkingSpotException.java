package com.api.parkingcontrol.exception;

public class DuplicateParkingSpotException extends RuntimeException{
    public DuplicateParkingSpotException(String message) {
        super(message);
    }
}
