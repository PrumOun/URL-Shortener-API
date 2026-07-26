package com.odev.urlshortener.demo.service;

public interface RedirectService {
    String getOriginalUrl(String shortCode);
}
