package unit.com.vanguard.weather.service;

import com.vanguard.weather.dto.WeatherApiResponse;
import com.vanguard.weather.dto.WeatherData;
import com.vanguard.weather.exception.NoDataFoundException;
import com.vanguard.weather.integration.WeatherMapClient;
import com.vanguard.weather.repository.WeatherRepository;
import com.vanguard.weather.dto.WeatherReport;
import com.vanguard.weather.entity.Weather;
import com.vanguard.weather.service.impl.WeatherServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceImplTest {

    @Mock
    private WeatherMapClient client;

    @Mock
    private WeatherRepository repository;

    @InjectMocks
    private WeatherServiceImpl service;

    @Test
    public void validParameters_success() {

        WeatherData data = WeatherData.builder().description("light rain").build();
        WeatherReport report = new WeatherReport();
        report.setWeather(List.of(data));
        when(client.execute(anyString(), anyString(), anyString())).thenReturn(report);

        Weather weather = Weather.builder()
            .weather("light rain")
            .build();
        when(repository.findWeatherByCityAndCountry(anyString(), anyString())).thenReturn(Optional.of(weather));

        WeatherApiResponse response = service.getWeatherReport("London", "uk", "ddfffb7a-73e4-4857-97fc-2aabffec438d");
        assertEquals(response.getDescription(), "light rain");
    }

    @Test
    public void emptyResponse_throwsNoDataFoundException() {
        WeatherReport report = new WeatherReport();
        when(client.execute(anyString(), anyString(), anyString())).thenReturn(report);

        NoDataFoundException exception = assertThrows(NoDataFoundException.class,
            () -> service.getWeatherReport("London", "uk", "ddfffb7a-73e4-4857-97fc-2aabffec438d"));

        assertEquals(exception.getMessage(), "No data found");
    }

    @Test
    public void dataNotFoundInDB_throwsNoDataFoundException() {
        WeatherData data = WeatherData.builder().description("light rain").build();
        WeatherReport report = new WeatherReport();
        report.setWeather(List.of(data));
        when(client.execute(anyString(), anyString(), anyString())).thenReturn(report);

        when(repository.findWeatherByCityAndCountry(anyString(), anyString())).thenReturn(Optional.empty());

        NoDataFoundException exception = assertThrows(NoDataFoundException.class,
            () -> service.getWeatherReport("London", "uk", "ddfffb7a-73e4-4857-97fc-2aabffec438d"));
        assertEquals(exception.getMessage(), "No data found");
    }

}
