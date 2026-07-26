package com.odev.urlshortener.demo.service.impl;

import com.odev.urlshortener.demo.entity.UrlEntity;
import com.odev.urlshortener.demo.exception.UrlGoneException;
import com.odev.urlshortener.demo.exception.UrlNotFoundException;
import com.odev.urlshortener.demo.repository.UrlRepository;
import com.odev.urlshortener.demo.service.RedirectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedirectServiceImpl implements RedirectService {
    private final UrlRepository urlRepository;

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
        // 5. Increment click count and save
        entity.setClickCount(entity.getClickCount() + 1);
        urlRepository.save(entity);
        // 6. Return the original URL
        return entity.getOriginalUrl();
    }
}
