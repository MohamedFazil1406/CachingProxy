package com.example.cachingProxy.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CachedResponse {

    private String body;

    private int statusCode;

    private long timestamp;
}