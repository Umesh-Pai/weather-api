package com.vanguard.weather.service;

import com.vanguard.weather.dto.WeatherApiResponse;

public interface WeatherService {

    WeatherApiResponse getWeatherReport(String city, String country, String correlationId);

}
