package com.odev.urlshortener.demo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Details of a shortened URL")
public class UrlResponse {
    @Schema(description = "Unique database identifier", example = "1")
    private Long id;

    @Schema(description = "The original long URL", example = "https://spring.io")
    private String originalUrl;

    @Schema(description = "Generated short code", example = "AbC123")
    private String shortCode;

    @Schema(description = "Full clickable short URL", example = "http://localhost:8080/r/AbC123")
    private String shortUrl;

    @Schema(description = "Total number of times this link has been clicked", example = "125")
    private Long clickCount;

    @Schema(description = "Timestamp when the URL was created", example = "2026-08-02T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Whether this URL has been soft-deleted", example = "false")
    private Boolean deleted;
}
