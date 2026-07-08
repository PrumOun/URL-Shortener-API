package com.odev.urlshortener.demo.service.impl;

import com.odev.urlshortener.demo.dto.request.UrlShortenRequest;
import com.odev.urlshortener.demo.dto.response.UrlShortenResponse;
import com.odev.urlshortener.demo.entity.UrlEntity;
import com.odev.urlshortener.demo.repository.UrlRepository;
import com.odev.urlshortener.demo.service.UrlService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.odev.urlshortener.demo.util.ShortCodeGenerator.generate;

@Service
public class UrlServiceImpl implements UrlService {
    private final UrlRepository urlRepository;
    @Value("${app.base-url}")
    private String baseUrl;

    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public UrlShortenResponse shortenUrl(UrlShortenRequest request) {
        // 1. Receive DTO -> already done (method parameter)
        //    Validation itself happens at the Controller via @Valid

        // 2. Generate Short Code (retry on the rare collision)
        String shortCode = generateUniqueShortCode();

        // 3. Create Entity and save to DB
        UrlEntity entity = new UrlEntity();
        entity.setOriginalUrl(request.getOriginalUrl());
        entity.setShortCode(shortCode);
        entity.setClickCount(0L);

        UrlEntity saved = urlRepository.save(entity);

        // 4, Build Response DTO
        UrlShortenResponse response = new UrlShortenResponse();
        response.setId(saved.getId());
        response.setOriginalUrl(saved.getOriginalUrl());
        response.setShortCode(baseUrl + "/" + saved.getShortCode());
        response.setClickCount(saved.getClickCount());
        response.setCreatedAt(saved.getCreatedAt());

        // 5. Return Response DTO
        return response;
    }

    private String generateUniqueShortCode() {
        String shortCode;
        do {
            shortCode = generate();
        } while (urlRepository.findByShortCode(shortCode).isPresent());
        return shortCode;
    }
}
