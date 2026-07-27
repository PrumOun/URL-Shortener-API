package com.odev.urlshortener.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TopUrlResponse {
    private String shortCode;
    private Long clickCount;
}
