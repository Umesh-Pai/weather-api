# Weather API

Weather API enables a consumer to retrieve the weather report for a particular city.

## Getting Started

### Prerequisite tooling

- Any SpringBoot/Java IDE. Ideally IntelliJ IDEA.
- Java 17
- Gradle 8
- SpringBoot 3.2.5


## Running the application

Run the following command from the folder containing the build.gradle file.

./gradlew bootRun

## Invoking the API

The API can be invoked using the url - 
http://localhost:8090/v1/weather-api/weather?city=London&country=uk

## OpenAPI Specification

OpenAPI Specification can be accessed using the URL
http://localhost:8090/swagger-ui.html

## Future Enhancements

- Specify all the error codes and details in the swagger spec
- Add Mapped Diagnostic Context (MDC) and store correlation Id, city name and country name
- Add test cases for testing resilience4j CircuitBreaker and Retry
- Add more test cases for code coverage