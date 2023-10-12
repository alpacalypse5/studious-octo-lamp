package com.ujjwalbhardwaj.intuit.profile.repository.cache;

import com.ujjwalbhardwaj.intuit.profile.entity.Profile;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfileCacheRepositoryTest {
    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RMapCache<String, Profile> cache;

    @Mock
    private ProfileRepository profileRepository;

    private ProfileCacheRepository profileCacheRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.doReturn(cache).when(redissonClient).getMapCache(anyString());

        profileCacheRepository = new ProfileCacheRepository(redissonClient, profileRepository, "cacheName", 3600);
    }

    @Test
    public void testContainsKey() {
        String key = "someKey";
        when(cache.containsKey(key)).thenReturn(true);
        boolean result = profileCacheRepository.containsKey(key);

        assertTrue(result);
        verify(cache).containsKey(key);
    }

    @Test
    public void testPut() {
        String key = "someKey";
        Profile profile = new Profile();
        Mockito.doReturn(cache).when(redissonClient).getMapCache(anyString());
        profileCacheRepository.put(key, profile);

        verify(cache).put(eq(key), eq(profile), anyLong(), any());
    }

    @Test
    public void testGet_ExistingInCache() {
        String key = "someKey";
        Profile profile = new Profile();
        when(cache.containsKey(key)).thenReturn(true);
        when(cache.get(key)).thenReturn(profile);
        Mockito.doReturn(cache).when(redissonClient).getMapCache(anyString());

        Profile result = profileCacheRepository.get(key);

        assertEquals(profile, result);
        verify(profileRepository, never()).findById(key);
    }

    @Test
    public void testGet_NotExistingInCacheButInDB() {
        String key = "someKey";
        Profile profile = new Profile();
        when(cache.containsKey(key)).thenReturn(false);
        when(profileRepository.findById(key)).thenReturn(Optional.of(profile));
        Mockito.doReturn(cache).when(redissonClient).getMapCache(anyString());

        Profile result = profileCacheRepository.get(key);

        verify(cache).put(key, profile);
    }

    @Test
    public void testGet_NotExistingAnywhere() {
        String key = "someKey";
        when(cache.containsKey(key)).thenReturn(false);
        when(profileRepository.findById(key)).thenReturn(Optional.empty());
        Mockito.doReturn(cache).when(redissonClient).getMapCache(anyString());

        Profile result = profileCacheRepository.get(key);

        assertNull(result);
        verify(cache, never()).put(eq(key), any());
    }
}
