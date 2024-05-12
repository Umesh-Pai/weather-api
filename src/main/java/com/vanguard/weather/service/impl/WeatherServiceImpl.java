package com.vanguard.weather.service.impl;

import com.vanguard.weather.dto.WeatherApiResponse;
import com.vanguard.weather.exception.NoDataFoundException;
import com.vanguard.weather.integration.WeatherMapClient;
import com.vanguard.weather.repository.WeatherRepository;
import com.vanguard.weather.dto.WeatherReport;
import com.vanguard.weather.entity.Weather;
import com.vanguard.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository repository;

    private final WeatherMapClient client;

    public WeatherApiResponse getWeatherReport(String city, String country, String correlationId) {

        // Get response from Open map
        WeatherReport response = client.execute(city, country, correlationId);
        log.info("Response received from WeatherMap service: {}, correlationId{}", response, correlationId);
        if (null == response.getWeather() || response.getWeather().isEmpty()) {
            log.error("Response from WeatherMap for city:{} and country:{} is blank, correlationId:{}", city, country, correlationId);
            throw new NoDataFoundException("No data found");
        }

        // save the data in DB
        repository.save(Weather.builder()
            .city(city)
            .country(country)
            .weather(response.getWeather().get(0).getDescription())
            .build());
        log.info("Weather data inserted successfully, correlationId:{}", correlationId);

        // Get data from DB
        Weather weather = repository.findWeatherByCityAndCountry(city, country)
                .orElseThrow(() -> {
                    log.error("No data found in database for city:{} and country:{}, correlationId:{}", city, country, correlationId);
                    return new NoDataFoundException("No data found");
                });
        return WeatherApiResponse.builder().description(weather.getWeather()).build();
    }

}
