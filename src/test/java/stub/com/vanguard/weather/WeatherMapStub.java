package stub.com.vanguard.weather;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class WeatherMapStub {

    private static WireMockServer weatherMapStubServer;

    public static WireMockServer getWeatherMapStubServer() {

        if (weatherMapStubServer == null) {
            WireMockConfiguration wireMockConfiguration = options()
                .port(8091)
                .bindAddress("localhost")
                .usingFilesUnderClasspath("src/test/resources/stubdata/weatherMap");
            weatherMapStubServer = new WireMockServer(wireMockConfiguration);
        }
        return weatherMapStubServer;
    }
}
