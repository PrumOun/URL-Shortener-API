package com.odev.urlshortener.demo.controller;

import com.odev.urlshortener.demo.dto.response.TopUrlResponse;
import com.odev.urlshortener.demo.dto.response.UrlStatsResponse;
import com.odev.urlshortener.demo.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "View statistics and rankings for shortened URLs")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @Operation(summary = "Get URL statistics", description = "Returns click count, creation/update timestamps, expiration, and deleted status for a given short code.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stats returned successfully"),
            @ApiResponse(responseCode = "404", description = "Short code does not exist")
    })
    @GetMapping("/api/analytics/{shortCode}/stats")
    public ResponseEntity<UrlStatsResponse> getUrlStats(@PathVariable String shortCode){
        UrlStatsResponse urlStatsResponse = analyticsService.getUrlStats(shortCode);
        return ResponseEntity.ok(urlStatsResponse);
    }

    @Operation(summary = "Get most clicked URLs", description = "Returns the top N URLs ranked by click count, sorted in descending order. Deleted URLs are excluded.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Top URLs returned successfully"),
            @ApiResponse(responseCode = "400", description = "limit is not positive or exceeds the maximum allowed value")
    })
    @GetMapping("/api/analytics/top")
    public ResponseEntity<List<TopUrlResponse>> getTopUrls(@RequestParam(defaultValue = "10") int limit) {
        List<TopUrlResponse> topUrls = analyticsService.getTopUrls(limit);
        return ResponseEntity.ok(topUrls);
    }
}
