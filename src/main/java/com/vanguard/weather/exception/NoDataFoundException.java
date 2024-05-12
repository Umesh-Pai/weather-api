package com.vanguard.weather.exception;

public class NoDataFoundException extends RuntimeException {

    public NoDataFoundException(final String message) {
        super(message);
    }

}
