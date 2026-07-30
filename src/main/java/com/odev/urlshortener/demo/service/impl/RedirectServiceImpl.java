package com.odev.urlshortener.demo.service.impl;

import com.odev.urlshortener.demo.entity.UrlEntity;
import com.odev.urlshortener.demo.exception.UrlGoneException;
import com.odev.urlshortener.demo.exception.UrlNotFoundException;
import com.odev.urlshortener.demo.repository.UrlRepository;
import com.odev.urlshortener.demo.service.RedirectService;
import lombok.AllArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
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
            throw new UrlGoneException(shortCode);
        }
        if(entity.getExpiresAt() != null && entity.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new UrlGoneException(shortCode);
        }

        // 4. get the original URL
        String originalUrl = entity.getOriginalUrl();

        // 5. Increment click count and save
        urlRepository.incrementClickCount(shortCode);

        return originalUrl;
    }

    private void tryIncrementAndGetUrl(String shortCode){
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                UrlEntity entity = urlRepository.findByShortCode(shortCode)
                        .orElseThrow(() -> new UrlNotFoundException(shortCode));

                entity.setClickCount(entity.getClickCount() + 1);
                urlRepository.save(entity);
                return;
            } catch (ObjectOptimisticLockingFailureException e) {
                if (attempt == MAX_RETRIES) {
                    System.err.println("Failed to increment click count for " + shortCode + " after " + MAX_RETRIES + " attempts");
                    return;
                }
                // Optionally log the retry attempt
            }
        }
    }

}
