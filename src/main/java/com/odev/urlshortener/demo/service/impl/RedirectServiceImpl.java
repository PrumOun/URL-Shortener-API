package com.odev.urlshortener.demo.service.impl;

import com.odev.urlshortener.demo.entity.UrlEntity;
import com.odev.urlshortener.demo.exception.UrlGoneException;
import com.odev.urlshortener.demo.exception.UrlNotFoundException;
import com.odev.urlshortener.demo.repository.UrlRepository;
import com.odev.urlshortener.demo.service.RedirectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class RedirectServiceImpl implements RedirectService {
    private final UrlRepository urlRepository;

    private static final int MAX_RETRIES = 3;

    @Override
    public String getOriginalUrl(String shortCode) {
        // 1. Input shortCode -> already done (method parameter)
        // 2. Query DB for the original URL
        UrlEntity entity = urlRepository.findByShortCode(shortCode)
                // 3. If not found, throw UrlNotFoundException
                .orElseThrow(() -> new UrlNotFoundException(shortCode));

        if(entity.isDeleted()){
            log.warn("Redirect blocked - URL deleted: shortCode={}", shortCode);
            throw new UrlGoneException(shortCode);
        }
        if(entity.getExpiresAt() != null && entity.getExpiresAt().isBefore(LocalDateTime.now())){
            log.warn("Redirect blocked - URL expired: shortCode={}", shortCode);
            throw new UrlGoneException(shortCode);
        }

        // 4. get the original URL
        String originalUrl = entity.getOriginalUrl();
        log.info("Redirect requested: shortCode={}", shortCode);

        // 5. Increment click count and save
        urlRepository.incrementClickCount(shortCode);

        return originalUrl;
    }

}
