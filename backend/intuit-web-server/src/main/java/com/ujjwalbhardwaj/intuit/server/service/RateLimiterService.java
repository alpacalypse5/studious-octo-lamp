package com.ujjwalbhardwaj.intuit.server.service;

import io.github.bucket4j.*;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class RateLimiterService {
    private final ProxyManager<String> buckets;

    public Bucket resolveBucket(String key) {
        Supplier<BucketConfiguration> configSupplier = getConfigSupplierForUser(key);
        return buckets.builder().build(key, configSupplier);
    }

    private Supplier<BucketConfiguration> getConfigSupplierForUser(String key) {

        Refill refill = Refill.intervally(2, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(2, refill);
        return () -> (BucketConfiguration.builder()
                .addLimit(limit)
                .build());
    }
}

