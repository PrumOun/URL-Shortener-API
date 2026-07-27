package com.odev.urlshortener.demo.controller;

import com.odev.urlshortener.demo.dto.response.TopUrlResponse;
import com.odev.urlshortener.demo.dto.response.UrlStatsResponse;
import com.odev.urlshortener.demo.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/api/analytics/{shortCode}/stats")
    public ResponseEntity<UrlStatsResponse> getUrlStats(@PathVariable String shortCode){
        UrlStatsResponse urlStatsResponse = analyticsService.getUrlStats(shortCode);
        return ResponseEntity.ok(urlStatsResponse);
    }

    @GetMapping("/api/analytics/top")
    public ResponseEntity<List<TopUrlResponse>> getTopUrls(@RequestParam(defaultValue = "10") int limit) {
        List<TopUrlResponse> topUrls = analyticsService.getTopUrls(limit);
        return ResponseEntity.ok(topUrls);
    }
}
