package com.odev.urlshortener.demo.service.impl;

import com.odev.urlshortener.demo.dto.response.TopUrlResponse;
import com.odev.urlshortener.demo.dto.response.UrlStatsResponse;
import com.odev.urlshortener.demo.entity.UrlEntity;
import com.odev.urlshortener.demo.exception.InvalidPageSizeException;
import com.odev.urlshortener.demo.exception.UrlNotFoundException;
import com.odev.urlshortener.demo.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceImplTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    @Test
    void getStats_shouldReturnStats_whenUrlExists() {
        UrlEntity entity = new UrlEntity();
        entity.setShortCode("AbC123");
        entity.setOriginalUrl("https://spring.io");
        entity.setClickCount(125L);
        entity.setDeleted(false);

        when(urlRepository.findByShortCode("AbC123")).thenReturn(Optional.of(entity));

        UrlStatsResponse response = analyticsService.getUrlStats("AbC123");

        assertThat(response.getShortCode()).isEqualTo("AbC123");
        assertThat(response.getClickCount()).isEqualTo(125L);
    }

    @Test
    void getStats_shouldThrow_whenUrlDoesNotExist() {
        when(urlRepository.findByShortCode("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> analyticsService.getUrlStats("missing"))
                .isInstanceOf(UrlNotFoundException.class);
    }

    @Test
    void getTopUrls_shouldReturnRankedList() {
        UrlEntity url1 = new UrlEntity();
        url1.setShortCode("spring");
        url1.setClickCount(230L);

        UrlEntity url2 = new UrlEntity();
        url2.setShortCode("java");
        url2.setClickCount(180L);

        when(urlRepository.findByDeletedFalseOrderByClickCountDesc(any(Pageable.class)))
                .thenReturn(List.of(url1, url2));

        List<TopUrlResponse> result = analyticsService.getTopUrls(10);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getShortCode()).isEqualTo("spring");
        assertThat(result.get(0).getClickCount()).isEqualTo(230L);
    }

    @Test
    void getTopUrls_shouldReturnEmptyList_whenNoUrlsExist() {
        when(urlRepository.findByDeletedFalseOrderByClickCountDesc(any(Pageable.class)))
                .thenReturn(Collections.emptyList());

        List<TopUrlResponse> result = analyticsService.getTopUrls(10);

        assertThat(result).isEmpty();
    }

    @Test
    void getTopUrls_shouldThrow_whenLimitExceedsMax() {
        assertThatThrownBy(() -> analyticsService.getTopUrls(1000))
                .isInstanceOf(InvalidPageSizeException.class);
    }
}
