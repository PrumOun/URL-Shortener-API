package com.odev.urlshortener.demo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Schema(description = "A paginated wrapper containing a page of results plus pagination metadata")
public class PageResponse<T> {
    @Schema(description = "The list of items for the current page")
    private List<T> content;

    @Schema(description = "Current page number (zero-based)", example = "0")
    private int pageNumber;

    @Schema(description = "Number of items requested per page", example = "10")
    private int pageSize;

    @Schema(description = "Total number of items across all pages", example = "42")
    private long totalElements;

    @Schema(description = "Total number of pages available", example = "5")
    private int totalPages;

    @Schema(description = "Whether this is the last page", example = "false")
    private boolean last;
}
