package com.odev.urlshortener.demo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class UrlShortenRequest {
    @Schema(description = "The original URL to be shortened", example = "https://spring.io", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Original URL cannot be blank")
    @URL(message = "URL must be a valid URL")
    private String originalUrl;
}
