package com.odev.urlshortener.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class UrlShortenRequest {
    @NotBlank(message = "Original URL cannot be blank")
    @URL(message = "URL must be a valid URL")
    private String originalUrl;
}
