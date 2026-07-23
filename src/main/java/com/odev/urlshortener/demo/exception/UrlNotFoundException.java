package com.odev.urlshortener.demo.exception;

public class UrlNotFoundException extends RuntimeException{
    public UrlNotFoundException(String shortCode) {
        super("No URL found for short code: " + shortCode);
    }
    public UrlNotFoundException(Long id) {
        super("No URL found for id: " + id);
    }
}
