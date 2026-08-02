package com.odev.urlshortener.demo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Standard error response returned for any failed request")
public class ErrorResponse {
    @Schema(description = "The exact moment the error occurred", example = "2026-08-02T10:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "HTTP status reason phrase", example = "Not Found")
    private String error;

    @Schema(description = "Human-readable explanation of what went wrong", example = "No URL found for short code: AbC123")
    private String message;

    @Schema(description = "The request path that caused the error", example = "/r/AbC123")
    private String path;
}
