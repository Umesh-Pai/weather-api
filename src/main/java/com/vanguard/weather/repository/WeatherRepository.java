package com.vanguard.weather.repository;

import com.vanguard.weather.entity.Weather;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, String> {

    Optional<Weather> findWeatherByCityAndCountry(String city, String country);
}
