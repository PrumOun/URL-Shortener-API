package com.odev.urlshortener.demo.controller;

import com.odev.urlshortener.demo.service.RedirectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Tag(name = "Redirect", description = "Redirects a short code to its original URL")
public class RedirectController {
    private final RedirectService redirectService;

    @Operation(summary = "Redirect to original URL", description = "Given a short code, redirects (HTTP 302) to the original URL and increments its click count.")
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "Redirect issued successfully"),
            @ApiResponse(responseCode = "404", description = "Short code does not exist"),
            @ApiResponse(responseCode = "410", description = "URL has been deleted or has expired")
    })
    @GetMapping("/r/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode) {
        String originalUrl = redirectService.getOriginalUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
    }
}
