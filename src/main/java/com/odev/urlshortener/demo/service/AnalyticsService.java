package com.odev.urlshortener.demo.service;

import com.odev.urlshortener.demo.dto.response.UrlStatsResponse;
import org.springframework.stereotype.Service;

public interface AnalyticsService {
    UrlStatsResponse getUrlStats(String shortCode);
}
