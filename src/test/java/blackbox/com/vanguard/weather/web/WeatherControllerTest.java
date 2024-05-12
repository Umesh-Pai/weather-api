package blackbox.com.vanguard.weather.web;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;

import com.vanguard.weather.WeatherApplication;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import stub.com.vanguard.weather.WeatherMapStub;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {WeatherApplication.class})
public class WeatherControllerTest {

    @LocalServerPort
    private int port;

    private static String BASE_URI = "http://localhost";

    private static String V1_BASE_PATH = "/v1/weather-api";

    private static String WEATHER_URL = "/weather";

    private static final String X_CORRELATION_ID_HEADER = "X-CorrelationID";

    private static final String X_APIKEY_HEADER = "X-API-Key";

    @Value("${weather-map.service.path}")
    private String weatherMapPath;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.basePath = V1_BASE_PATH;
        RestAssured.port = port;

        WeatherMapStub.getWeatherMapStubServer().start();
    }

    @AfterEach
    void cleanUp() {
        WeatherMapStub.getWeatherMapStubServer().stop();
    }

    @Test
    void missingApiKey_returns400() {
        given()
            .log().all()
            .header(X_CORRELATION_ID_HEADER, "12345")
            .when()
            .get(WEATHER_URL + "?city=London&country=uk")
            .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .contentType(JSON)
            .header(X_CORRELATION_ID_HEADER, equalTo("12345"))
            .body("errorMessage",  equalTo("Missing Header: X-API-Key"));
    }

    @Test
    void invalidApiKey_returns401() {
        given()
            .log().all()
            .header(X_CORRELATION_ID_HEADER, "12345")
            .header(X_APIKEY_HEADER, "test")
            .when()
            .get(WEATHER_URL + "?city=London&country=uk")
            .then()
            .log().all()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
            .contentType(JSON)
            .header(X_CORRELATION_ID_HEADER, equalTo("12345"))
            .body("errorMessage",  equalTo("Invalid Header value: X-API-Key"));
    }

    @Test
    void missingParameterCity_returns400() {
        given()
            .log().all()
            .header(X_CORRELATION_ID_HEADER, "12345")
            .header(X_APIKEY_HEADER, "db69b8c7a0722ce1007814439a558418")
            .when()
            .get(WEATHER_URL + "?ci=London&country=uk")
            .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .contentType(JSON)
            .header(X_CORRELATION_ID_HEADER, equalTo("12345"))
            .body("errorMessage",  equalTo("Required request parameter 'city' for method parameter type String is not present"));
    }

    @Test
    void validRequest_success() {
        given()
            .log().all()
            .header(X_CORRELATION_ID_HEADER, "12345")
            .header(X_APIKEY_HEADER, "db69b8c7a0722ce1007814439a558418")
            .when()
            .get(WEATHER_URL + "?city=London&country=uk")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .contentType(JSON)
            .header(X_CORRELATION_ID_HEADER, equalTo("12345"))
            .body("description",  equalTo("light intensity drizzle"));
    }

}
