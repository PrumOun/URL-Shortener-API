package com.odev.urlshortener.demo.exception;

public class InvalidPageSizeException extends RuntimeException {
    public InvalidPageSizeException(int size, int maxAllowed) {
        super("Page size " + size + " exceeds maximum allowed size of " + maxAllowed);
    }

    public InvalidPageSizeException(String message) {
        super(message);
    }
}
