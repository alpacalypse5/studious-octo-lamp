package com.ujjwalbhardwaj.intuit.profile.repository.cache;

import com.ujjwalbhardwaj.intuit.profile.entity.Profile;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileRepository;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class ProfileCacheRepository implements CacheRepository<Profile> {
    private final RMapCache<String, Profile> cache;
    private final long timeToLive;
    private final ProfileRepository profileRepository;

    public ProfileCacheRepository(final RedissonClient redissonClient,
                                  final ProfileRepository profileRepository,
                                  @Value("${profile.cache.name}") final String cacheName,
                                  @Value("${profile.cache.ttl}") final long timeToLive) {
        this.timeToLive = timeToLive;
        this.cache = redissonClient.getMapCache(cacheName);
        this.profileRepository = profileRepository;
    }

    @Override
    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    @Override
    public void put(String key, Profile value) {
        cache.put(key, value, timeToLive, TimeUnit.SECONDS);
    }

    @Override
    public Profile get(String key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        final Profile profile = profileRepository.findById(key).orElse(null);
        if (Objects.isNull(profile)) {
            return null;
        }
        cache.put(key, profile);
        return cache.get(key);
    }
}
