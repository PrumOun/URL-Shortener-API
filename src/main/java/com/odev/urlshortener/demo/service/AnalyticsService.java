package com.odev.urlshortener.demo.service;

import com.odev.urlshortener.demo.dto.response.TopUrlResponse;
import com.odev.urlshortener.demo.dto.response.UrlStatsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AnalyticsService {
    UrlStatsResponse getUrlStats(String shortCode);
    List<TopUrlResponse> getTopUrls(int limit);
}
