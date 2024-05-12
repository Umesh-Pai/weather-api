package com.vanguard.weather.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiError {

    private String errorMessage;
    private int status;
    private String title;
    private String path;

}
