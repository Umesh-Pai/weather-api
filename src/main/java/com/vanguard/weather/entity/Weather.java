package com.vanguard.weather.entity;

import com.vanguard.weather.model.WeatherId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(WeatherId.class)
public class Weather {

    @Id
    @Column(name="city")
    private String city;

    @Id
    @Column(name="country")
    private String country;

    @Column(name="weather")
    private String weather;
}
