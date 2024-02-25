package com.swiftcart.swiftcart.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import java.time.Duration;

public class RateLimitService {
    private static final long CAPACITY = 30;
    private static final Duration REFILL_PERIOD = Duration.ofMinutes(1);
    private static final long REFILL_TOKEN = 30;
    private final Bucket bucket;

    public RateLimitService() {
        Bandwidth limit = Bandwidth.classic(CAPACITY, Refill.intervally(REFILL_TOKEN, REFILL_PERIOD));
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    public boolean tryConsume() {
        return bucket.tryConsume(1);
    }


}
