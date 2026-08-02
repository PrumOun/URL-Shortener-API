package com.odev.urlshortener.demo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "A single entry in the most-clicked URLs ranking")
public class TopUrlResponse {
    @Schema(description = "The generated short code", example = "spring")
    private String shortCode;

    @Schema(description = "Total number of times this link has been clicked", example = "230")
    private Long clickCount;
}
