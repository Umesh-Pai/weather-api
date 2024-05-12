package com.vanguard.weather.interceptor;

import com.vanguard.weather.exception.InvalidRequestException;
import com.vanguard.weather.exception.RateLimitException;
import com.vanguard.weather.exception.UnauthorizedException;
import com.vanguard.weather.config.ClientProps;
import com.vanguard.weather.service.RateLimiterService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiterService rateLimiterService;

    private final ClientProps clientProps;

    private static final String X_APIKEY_HEADER = "X-API-Key";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String apiKey = request.getHeader(X_APIKEY_HEADER);
        log.info("Request received with apikey:{}", apiKey);

        if (StringUtils.isBlank(apiKey)) {
            log.error("No apikey passed in the request");
            throw new InvalidRequestException("Missing Header: " + X_APIKEY_HEADER);
        }

        if(!clientProps.getApiKeys().contains(apiKey)) {
            log.error("Invalid apikey passed in the request");
            throw new UnauthorizedException("Invalid Header value: " + X_APIKEY_HEADER);
        }

        Bucket bucket = rateLimiterService.resolveBucket(apiKey);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if(probe.isConsumed()) {
            return true;
        } else {
            log.error("API Request Quota exhausted");
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            throw new RateLimitException("You have exhausted your API Request Quota");
        }
    }
}
