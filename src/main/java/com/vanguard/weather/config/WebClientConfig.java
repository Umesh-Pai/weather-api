package com.vanguard.weather.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${weather-map.service.endpoint}")
    private String weatherMapEndpoint;

    @Bean
    public WebClient webclient(WebClient.Builder webClient) {
        return webClient.baseUrl(weatherMapEndpoint)
            .build();
    }

}
