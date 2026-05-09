package com.example.cachingProxy.controller;

import com.example.cachingProxy.service.ProxyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProxyController {

    private final ProxyService proxyService;

    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @GetMapping("/**")
    public ResponseEntity<String> proxy(HttpServletRequest request) {

        String path = request.getRequestURI();

        return proxyService.forwardRequest(path);
    }
}