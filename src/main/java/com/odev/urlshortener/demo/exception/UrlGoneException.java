package com.odev.urlshortener.demo.exception;

public class UrlGoneException extends RuntimeException {
    public UrlGoneException(String shortCode) {
        super("The URL is no longer active or has been deleted: " + shortCode);
    }
}
