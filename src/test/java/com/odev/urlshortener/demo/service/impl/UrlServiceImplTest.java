package com.odev.urlshortener.demo.service.impl;

import com.odev.urlshortener.demo.dto.request.UrlShortenRequest;
import com.odev.urlshortener.demo.dto.response.UrlResponse;
import com.odev.urlshortener.demo.entity.UrlEntity;
import com.odev.urlshortener.demo.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlServiceImpl urlService;

    @BeforeEach
    void setUp() {
        // manually inject the @Value field since Mockito doesn't process it
        ReflectionTestUtils.setField(urlService, "baseUrl", "http://localhost:8080");
    }

    @Test
    void shortenUrl_shouldCreateUrlSuccessfully() {
        // Arrange
        UrlShortenRequest request = new UrlShortenRequest();
        request.setOriginalUrl("https://spring.io");

        when(urlRepository.findByShortCode(anyString())).thenReturn(Optional.empty());
        when(urlRepository.save(any(UrlEntity.class))).thenAnswer(invocation -> {
            UrlEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });

        // Act
        UrlResponse response = urlService.shortenUrl(request);

        // Assert
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getOriginalUrl()).isEqualTo("https://spring.io");
        assertThat(response.getShortCode()).isNotBlank();
        assertThat(response.getShortUrl()).contains(response.getShortCode());
        assertThat(response.getClickCount()).isEqualTo(0L);
        assertThat(response.getDeleted()).isFalse();

        verify(urlRepository, times(1)).save(any(UrlEntity.class));
    }

    @Test
    void shortenUrl_shouldGenerateUniqueShortCode_whenCollisionOccurs() {
        // Arrange — simulate: first generated code already exists, second doesn't
        UrlShortenRequest request = new UrlShortenRequest();
        request.setOriginalUrl("https://spring.io");

        UrlEntity existing = new UrlEntity();
        existing.setShortCode("DUPLICATE");

        when(urlRepository.findByShortCode(anyString()))
                .thenReturn(Optional.of(existing))  // first attempt: collision
                .thenReturn(Optional.empty());       // second attempt: free

        when(urlRepository.save(any(UrlEntity.class))).thenAnswer(invocation -> {
            UrlEntity entity = invocation.getArgument(0);
            entity.setId(2L);
            return entity;
        });

        // Act
        UrlResponse response = urlService.shortenUrl(request);

        // Assert — the collision-retry loop ran at least twice
        verify(urlRepository, times(2)).findByShortCode(anyString());
        assertThat(response.getId()).isEqualTo(2L);
    }
}