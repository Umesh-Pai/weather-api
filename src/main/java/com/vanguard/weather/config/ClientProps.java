package com.vanguard.weather.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="weather-map.client")
@Data
public class ClientProps {

    private List<String> apiKeys;

}
