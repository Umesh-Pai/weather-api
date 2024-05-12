package com.vanguard.weather.service.impl;

import com.vanguard.weather.service.RateLimiterService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterServiceImpl implements RateLimiterService {

    Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    @Override
    public Bucket resolveBucket(String apiKey){
        return bucketCache.computeIfAbsent(apiKey, this::createBucket);
    }

    private Bucket createBucket(String s) {
        Bandwidth bandwidth = Bandwidth.builder()
            .capacity(5)
            .refillIntervally(5, Duration.ofHours(1))
            .build();
        return Bucket.builder()
            .addLimit(bandwidth)
            .build();
    }

}
