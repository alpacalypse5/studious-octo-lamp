package com.ujjwalbhardwaj.intuit.profile.repository.cache;

import org.springframework.stereotype.Component;

@Component
public interface CacheRepository<T> {
    boolean containsKey(String key);
    void put(String key, T value);
    T get(String key);
}
