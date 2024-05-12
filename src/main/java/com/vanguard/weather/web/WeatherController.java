package com.vanguard.weather.web;

import com.vanguard.weather.dto.WeatherApiResponse;
import com.vanguard.weather.service.impl.WeatherServiceImpl;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.util.HtmlUtils.htmlEscape;

@Slf4j
@RestController
@RequestMapping(path = "/v1/weather-api")
public class WeatherController {

    @Autowired
    WeatherServiceImpl weatherService;

    @GetMapping(value = "/weather", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody WeatherApiResponse getWeatherReport(@RequestParam @NotBlank final String city,
        @RequestParam @NotBlank final String country, @RequestHeader("X-API-Key") final String apiKey,
        @RequestHeader(required = false, value = "X-CorrelationID") final String correlationId) {

        log.info("Request received for weather report for city:{} country:{}, correlationId:{}", city, country, correlationId);
        return weatherService.getWeatherReport(htmlEscape(city), htmlEscape(country), correlationId);
    }

}
