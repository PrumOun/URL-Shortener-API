package com.odev.urlshortener.demo.service;

import com.odev.urlshortener.demo.dto.request.UrlShortenRequest;
import com.odev.urlshortener.demo.dto.response.PageResponse;
import com.odev.urlshortener.demo.dto.response.UrlResponse;

public interface UrlService {
    UrlResponse shortenUrl(UrlShortenRequest request);
    String getOriginalUrl(String shortCode);
    UrlResponse getUrlDetails(Long id);
    PageResponse<UrlResponse> getAllUrls(int page, int size, String sort);
    void deleteUrl(Long id);
}
