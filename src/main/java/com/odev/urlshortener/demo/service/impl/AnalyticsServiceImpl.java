package com.odev.urlshortener.demo.service.impl;

import com.odev.urlshortener.demo.dto.response.UrlStatsResponse;
import com.odev.urlshortener.demo.entity.UrlEntity;
import com.odev.urlshortener.demo.exception.UrlNotFoundException;
import com.odev.urlshortener.demo.repository.UrlRepository;
import com.odev.urlshortener.demo.service.AnalyticsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {
    private final UrlRepository urlRepository;

    @Override
    public UrlStatsResponse getUrlStats(String shortCode){
        UrlEntity entity = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));

        return toStatesResponse(entity);
    }

    private UrlStatsResponse toStatesResponse(UrlEntity entity){
        UrlStatsResponse response = new UrlStatsResponse();
        response.setShortCode(entity.getShortCode());
        response.setOriginalUrl(entity.getOriginalUrl());
        response.setClickCount(entity.getClickCount());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setExpiresAt(entity.getExpiresAt());
        response.setDeleted(entity.isDeleted());
        return response;
    }
}
