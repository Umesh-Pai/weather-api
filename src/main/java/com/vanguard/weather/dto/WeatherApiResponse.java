package com.vanguard.weather.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WeatherApiResponse {

    private String description;
}
