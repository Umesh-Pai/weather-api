package com.vanguard.weather.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherData {

    private int id;
    private String main;
    private String description;
    private String icon;

}
