package com.odev.urlshortener.demo.service.impl;

import com.odev.urlshortener.demo.entity.UrlEntity;
import com.odev.urlshortener.demo.exception.UrlGoneException;
import com.odev.urlshortener.demo.exception.UrlNotFoundException;
import com.odev.urlshortener.demo.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedirectServiceImplTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private RedirectServiceImpl redirectService;

    @Test
    void getOriginalUrl_shouldReturnUrl_whenUrlExistsAndActive() {
        UrlEntity entity = new UrlEntity();
        entity.setShortCode("AbC123");
        entity.setOriginalUrl("https://spring.io");
        entity.setDeleted(false);
        entity.setExpiresAt(LocalDateTime.now().plusDays(30));

        when(urlRepository.findByShortCode("AbC123")).thenReturn(Optional.of(entity));

        String result = redirectService.getOriginalUrl("AbC123");

        assertThat(result).isEqualTo("https://spring.io");
        verify(urlRepository, times(1)).incrementClickCount("AbC123");
    }

    @Test
    void getOriginalUrl_shouldThrowNotFound_whenShortCodeMissing() {
        when(urlRepository.findByShortCode("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> redirectService.getOriginalUrl("missing"))
                .isInstanceOf(UrlNotFoundException.class);

        verify(urlRepository, never()).incrementClickCount(anyString());
    }

    @Test
    void getOriginalUrl_shouldThrowGone_whenUrlIsDeleted() {
        UrlEntity entity = new UrlEntity();
        entity.setShortCode("AbC123");
        entity.setDeleted(true);

        when(urlRepository.findByShortCode("AbC123")).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> redirectService.getOriginalUrl("AbC123"))
                .isInstanceOf(UrlGoneException.class);

        verify(urlRepository, never()).incrementClickCount(anyString());
    }

    @Test
    void getOriginalUrl_shouldThrowGone_whenUrlIsExpired() {
        UrlEntity entity = new UrlEntity();
        entity.setShortCode("AbC123");
        entity.setDeleted(false);
        entity.setExpiresAt(LocalDateTime.now().minusDays(1)); // expired yesterday

        when(urlRepository.findByShortCode("AbC123")).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> redirectService.getOriginalUrl("AbC123"))
                .isInstanceOf(UrlGoneException.class);

        verify(urlRepository, never()).incrementClickCount(anyString());
    }
}
