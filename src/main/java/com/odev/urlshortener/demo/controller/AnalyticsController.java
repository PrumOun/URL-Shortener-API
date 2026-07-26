package com.odev.urlshortener.demo.controller;

import com.odev.urlshortener.demo.dto.response.UrlStatsResponse;
import com.odev.urlshortener.demo.service.AnalyticsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/api/urls/{shortCode}/stats")
    public ResponseEntity<UrlStatsResponse> getUrlStats(@PathVariable String shortCode){
        UrlStatsResponse urlStatsResponse = analyticsService.getUrlStats(shortCode);
        return ResponseEntity.ok(urlStatsResponse);
    }
}
