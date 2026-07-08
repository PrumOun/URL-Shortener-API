package com.odev.urlshortener.demo.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class UrlShortenResponse {
    private Long id;
    private String originalUrl;
    private String shortCode;
    private Long clickCount;
    private LocalDateTime createdAt;
}
