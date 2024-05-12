package com.vanguard.weather.dto;

import java.util.List;
import lombok.Data;

@Data
public class WeatherReport {

    private List<WeatherData> weather;
}
