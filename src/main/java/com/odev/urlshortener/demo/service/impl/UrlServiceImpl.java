package com.odev.urlshortener.demo.service.impl;

import com.odev.urlshortener.demo.dto.request.UrlShortenRequest;
import com.odev.urlshortener.demo.dto.response.PageResponse;
import com.odev.urlshortener.demo.dto.response.UrlResponse;
import com.odev.urlshortener.demo.entity.UrlEntity;
import com.odev.urlshortener.demo.exception.InvalidPageSizeException;
import com.odev.urlshortener.demo.exception.InvalidSortFieldException;
import com.odev.urlshortener.demo.exception.UrlGoneException;
import com.odev.urlshortener.demo.exception.UrlNotFoundException;
import com.odev.urlshortener.demo.repository.UrlRepository;
import com.odev.urlshortener.demo.service.UrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.odev.urlshortener.demo.util.ShortCodeGenerator.generate;

@Service
@Slf4j
public class UrlServiceImpl implements UrlService {
    private final UrlRepository urlRepository;
    @Value("${app.base-url}")
    private String baseUrl;

    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public UrlResponse shortenUrl(UrlShortenRequest request) {
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
        log.info("URL created: shortCode={}, id={}", saved.getShortCode(), saved.getId());

        // 4. Return Response DTO
        return toResponse(saved);
    }

    private String generateUniqueShortCode() {
        String shortCode;
        do {
            shortCode = generate();
        } while (urlRepository.findByShortCode(shortCode).isPresent());
        return shortCode;
    }

    private UrlResponse toResponse(UrlEntity entity) {
        UrlResponse response = new UrlResponse();
        response.setId(entity.getId());
        response.setOriginalUrl(entity.getOriginalUrl());
        response.setShortCode(entity.getShortCode());
        response.setShortUrl(baseUrl + "/r/" + entity.getShortCode());
        response.setClickCount(entity.getClickCount());
        response.setCreatedAt(entity.getCreatedAt());
        response.setDeleted(entity.isDeleted());
        return response;
    }

    @Override
    public UrlResponse getUrlDetails(Long id) {
        UrlEntity entity = urlRepository.findById(id)
            .orElseThrow(() -> new UrlNotFoundException(id));
        return toResponse(entity);
    }

    private static final int MAX_PAGE_SIZE = 100;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "shortCode", "clickCount", "createdAt");

    @Override
    public PageResponse<UrlResponse> getAllUrls(int page, int size, String sort){
        if (size > MAX_PAGE_SIZE) {
            throw new InvalidPageSizeException(size, MAX_PAGE_SIZE);
        }
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];

        if (!ALLOWED_SORT_FIELDS.contains(sortField)) {
            throw new InvalidSortFieldException(sortField);
        }

        Sort.Direction sortDirection = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField));
        Page<UrlEntity> entityPage = urlRepository.findAll(pageable);

        List<UrlResponse> content = entityPage.getContent().stream()
                .map(this::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                entityPage.getNumber(),
                entityPage.getSize(),
                entityPage.getTotalElements(),
                entityPage.getTotalPages(),
                entityPage.isLast()
        );
    }

    @Override
    public void deleteUrl(Long id) {
        UrlEntity entity = urlRepository.findById(id)
                .orElseThrow(() -> new UrlNotFoundException(id));
        entity.setDeleted(true);
        urlRepository.save(entity);

        log.info("URL deleted: id={}, shortCode={}", id, entity.getShortCode());
    }
}
