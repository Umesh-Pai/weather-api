package com.vanguard.weather.model;

import java.io.Serializable;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class WeatherId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String city;

    private String country;
}
