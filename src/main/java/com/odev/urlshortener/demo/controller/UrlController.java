package com.odev.urlshortener.demo.controller;

import com.odev.urlshortener.demo.dto.request.UrlShortenRequest;
import com.odev.urlshortener.demo.dto.response.UrlShortenResponse;
import com.odev.urlshortener.demo.service.UrlService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<UrlShortenResponse> shortenUrl(@Valid @RequestBody UrlShortenRequest request) {
        UrlShortenResponse response = urlService.shortenUrl(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
