package com.vanguard.weather.exception;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(final String message) {
        super(message);
    }

}
