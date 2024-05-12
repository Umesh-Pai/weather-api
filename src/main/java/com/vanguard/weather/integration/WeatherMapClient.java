package com.vanguard.weather.integration;

import com.vanguard.weather.dto.WeatherData;
import com.vanguard.weather.dto.WeatherReport;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WeatherMapClient {

    private final WebClient client;

    @Value("${weather-map.service.path}")
    private String weatherMapPath;

    @Value("${weather-map.service.api-key}")
    private String weatherMapApiKey;

    private static final String RESILIENCE_WEATHER_MAP_CLIENT = "weather-map-api";

    private static final String X_CORRELATION_ID_HEADER = "X-CorrelationID";

    @Retry(name = RESILIENCE_WEATHER_MAP_CLIENT)
    @CircuitBreaker(name = RESILIENCE_WEATHER_MAP_CLIENT, fallbackMethod = "weatherFallback")
    public WeatherReport execute(String city, String country, String correlationId) {

        return client.get()
            .uri(builder -> builder
                .path(weatherMapPath)
                .queryParam("q", city + "," + country)
                .queryParam("appid", weatherMapApiKey)
                .build())
            .header(X_CORRELATION_ID_HEADER, correlationId)
            .retrieve()
            .bodyToMono(WeatherReport.class)
            .block();
    }

    private WeatherReport weatherFallback(Exception ex) {
        log.error("Exception while connecting to WeatherMap Service. Exception: {}" + ex.getMessage());

        WeatherData data = WeatherData.builder().description("").build();
        WeatherReport report = new WeatherReport();
        report.setWeather(List.of(data));
        return report;
    }
}
