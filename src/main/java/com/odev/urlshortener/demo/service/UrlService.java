package com.odev.urlshortener.demo.service;

import com.odev.urlshortener.demo.dto.request.UrlShortenRequest;
import com.odev.urlshortener.demo.dto.response.UrlShortenResponse;

public interface UrlService {
    UrlShortenResponse shortenUrl(UrlShortenRequest request);
    String getOriginalUrl(String shortCode);
}
