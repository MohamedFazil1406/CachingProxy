package com.example.cachingProxy.service;

import com.example.cachingProxy.model.CachedResponse;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheService {

    private final Map<String, CachedResponse> cache = new ConcurrentHashMap<>();

    private static final long TTL = 5 * 60 * 1000;

    public CachedResponse get(String key) {

        CachedResponse cachedResponse = cache.get(key);

        if (cachedResponse == null) {
            return null;
        }

        long currentTime = System.currentTimeMillis();

        if (currentTime - cachedResponse.getTimestamp() > TTL) {
            cache.remove(key);
            System.out.println("CACHE EXPIRED: " + key);
            return null;
        }

        System.out.println("CACHE HIT: " + key);
        return cachedResponse;
    }

    public void put(String key, CachedResponse response) {
        cache.put(key, response);
        System.out.println("CACHE SAVED: " + key);
    }
}