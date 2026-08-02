package com.odev.urlshortener.demo.controller;

import com.odev.urlshortener.demo.dto.request.UrlShortenRequest;
import com.odev.urlshortener.demo.dto.response.UrlResponse;
import com.odev.urlshortener.demo.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "URL Management", description = "Create, retrieve, list, and delete shortened URLs")
public class UrlController {
    private final UrlService urlService;

    @Operation(summary = "Create a shortened URL", description = "Accepts an original URL and returns a generated short code and short URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "URL successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing originalUrl")
    })
    @PostMapping("/api/urls")
    public ResponseEntity<UrlResponse> shortenUrl(@Valid @RequestBody UrlShortenRequest request) {
        UrlResponse response = urlService.shortenUrl(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get URL details by ID", description = "Retrieves full details of a shortened URL using its database ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "URL found"),
            @ApiResponse(responseCode = "404", description = "No URL exists with the given ID")
    })
    @GetMapping("/api/urls/{id}")
    public ResponseEntity<UrlResponse> getUrlById(@PathVariable Long id){
        UrlResponse response = urlService.getUrlDetails(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "List URLs (paginated)", description = "Returns a paginated, sortable list of shortened URLs, including soft-deleted ones.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of URLs returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid sort field or page size exceeds maximum allowed")
    })
    @GetMapping("/api/urls")
    public ResponseEntity<?> getAllUrls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        return ResponseEntity.ok(urlService.getAllUrls(page, size, sort));
    }

    @Operation(summary = "Soft delete a URL", description = "Marks a URL as deleted. The record remains in the database for auditing/analytics purposes, but its redirect will no longer work.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "URL successfully marked as deleted"),
            @ApiResponse(responseCode = "404", description = "No URL exists with the given ID")
    })
    @DeleteMapping("/api/urls/{id}")
    public ResponseEntity<Void> deleteUrl(@PathVariable Long id) {
        urlService.deleteUrl(id);
        return ResponseEntity.noContent().build();
    }

}
