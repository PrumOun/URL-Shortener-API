package com.odev.urlshortener.demo.controller;

import com.odev.urlshortener.demo.dto.request.UrlShortenRequest;
import com.odev.urlshortener.demo.dto.response.UrlResponse;
import com.odev.urlshortener.demo.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping("/api/urls")
    public ResponseEntity<UrlResponse> shortenUrl(@Valid @RequestBody UrlShortenRequest request) {
        UrlResponse response = urlService.shortenUrl(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/urls/{id}")
    public ResponseEntity<UrlResponse> getUrlById(@PathVariable Long id){
        UrlResponse response = urlService.getUrlDetails(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/api/urls")
    public ResponseEntity<?> getAllUrls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        return ResponseEntity.ok(urlService.getAllUrls(page, size, sort));
    }

    @DeleteMapping("/api/urls/{id}")
    public ResponseEntity<Void> deleteUrl(@PathVariable Long id) {
        urlService.deleteUrl(id);
        return ResponseEntity.noContent().build();
    }

}
