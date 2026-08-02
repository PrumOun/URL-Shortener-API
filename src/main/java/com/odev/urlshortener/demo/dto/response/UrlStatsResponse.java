package com.odev.urlshortener.demo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Schema(description = "Analytics and status information for a single shortened URL")
public class UrlStatsResponse {
    @Schema(description = "The generated short code", example = "AbC123")
    private String shortCode;

    @Schema(description = "The original long URL", example = "https://spring.io")
    private String originalUrl;

    @Schema(description = "Total number of times this link has been clicked", example = "125")
    private Long clickCount;

    @Schema(description = "Timestamp when the URL was created", example = "2026-07-23T08:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the URL was last updated (e.g. clicked or deleted)", example = "2026-07-23T10:30:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Timestamp when this URL will expire and stop redirecting", example = "2026-08-23T08:00:00")
    private LocalDateTime expiresAt;

    @Schema(description = "Whether this URL has been soft-deleted", example = "false")
    private Boolean deleted;
}
