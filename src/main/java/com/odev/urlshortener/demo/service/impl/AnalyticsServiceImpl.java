package com.odev.urlshortener.demo.service.impl;

import com.odev.urlshortener.demo.dto.response.TopUrlResponse;
import com.odev.urlshortener.demo.dto.response.UrlStatsResponse;
import com.odev.urlshortener.demo.entity.UrlEntity;
import com.odev.urlshortener.demo.exception.InvalidPageSizeException;
import com.odev.urlshortener.demo.exception.UrlNotFoundException;
import com.odev.urlshortener.demo.repository.UrlRepository;
import com.odev.urlshortener.demo.service.AnalyticsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {
    private final UrlRepository urlRepository;

    @Override
    public UrlStatsResponse getUrlStats(String shortCode){
        log.info("Analytics requested: shortCode={}", shortCode);

        UrlEntity entity = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));

        return toStatesResponse(entity);
    }

    private static final int MAX_LIMIT = 10;

    @Override
    public List<TopUrlResponse> getTopUrls(int limit) {

        if (limit <= 0){
            throw new InvalidPageSizeException("limit must be greater than 0, but was: " + limit);
        }

        if (limit > MAX_LIMIT) {
            throw new InvalidPageSizeException("limit must be less than or equal to " + MAX_LIMIT + ", but was: " + limit);
        }

        Pageable pageable = PageRequest.of(0, limit);

        return urlRepository.findByDeletedFalseOrderByClickCountDesc(pageable)
                .stream()
                .map(entity -> new TopUrlResponse(entity.getShortCode(), entity.getClickCount()))
                .toList();
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
