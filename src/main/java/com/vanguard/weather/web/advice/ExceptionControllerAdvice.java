package com.vanguard.weather.web.advice;

import com.vanguard.weather.exception.ApiError;
import com.vanguard.weather.exception.InvalidRequestException;
import com.vanguard.weather.exception.NoDataFoundException;
import com.vanguard.weather.exception.UnauthorizedException;
import com.vanguard.weather.exception.RateLimitException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler({InvalidRequestException.class,
        MissingServletRequestParameterException.class,
        HandlerMethodValidationException.class})
    public ResponseEntity<ApiError> handleInvalidRequestException(final Exception exception,
        final HttpServletRequest request) {

        log.error("InvalidRequestException:{} ", exception.getMessage());
        return buildApiErrorResponse(exception, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ApiError> handleRateLimitException(final RateLimitException exception,
        final HttpServletRequest request) {

        log.error("RateLimitException:{} ", exception.getMessage());
        return buildApiErrorResponse(exception, HttpStatus.TOO_MANY_REQUESTS, request);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(final UnauthorizedException exception,
        final HttpServletRequest request) {

        log.error("UnauthorizedException:{} ", exception.getMessage());
        return buildApiErrorResponse(exception, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ApiError> handleNoDataFoundException(final NoDataFoundException exception,
        final HttpServletRequest request) {

        log.error("NoDataFoundException:{} ", exception.getMessage());
        return buildApiErrorResponse(exception, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaughtException(final Exception exception,
        final HttpServletRequest request) {

        log.error("Exception:{} ", exception.getMessage());
        return buildApiErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ApiError> buildApiErrorResponse(Exception exception,
        HttpStatus httpStatus, HttpServletRequest request) {

        ApiError errorResponse = ApiError.builder()
            .status(httpStatus.value())
            .title(httpStatus.getReasonPhrase())
            .errorMessage(exception.getMessage())
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
    
}



