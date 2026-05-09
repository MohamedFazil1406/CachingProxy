package com.example.cachingProxy.service;

import com.example.cachingProxy.model.CachedResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ProxyService {

    private final CacheService cacheService;

    private final WebClient webClient;

    private static final String TARGET_BASE_URL = "https://jsonplaceholder.typicode.com";

    public ProxyService(CacheService cacheService, WebClient webClient) {
        this.cacheService = cacheService;
        this.webClient = webClient;
    }

    public ResponseEntity<String> forwardRequest(String path) {

        String cacheKey = "GET:" + path;

        CachedResponse cachedResponse = cacheService.get(cacheKey);

        if (cachedResponse != null) {
            return ResponseEntity
                    .status(cachedResponse.getStatusCode())
                    .body(cachedResponse.getBody());
        }

        System.out.println("CACHE MISS: " + cacheKey);
        System.out.println("FORWARDING REQUEST TO ACTUAL SERVER");

        String url = TARGET_BASE_URL + path;

        String responseBody = webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        CachedResponse response = new CachedResponse(
                responseBody,
                200,
                System.currentTimeMillis()
        );

        cacheService.put(cacheKey, response);

        return ResponseEntity.ok(responseBody);
    }
}