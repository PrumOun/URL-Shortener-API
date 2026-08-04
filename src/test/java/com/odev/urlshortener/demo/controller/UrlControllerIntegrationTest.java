package com.odev.urlshortener.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.odev.urlshortener.demo.dto.request.UrlShortenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UrlControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createUrl_shouldReturn201_whenValidRequest() throws Exception {
        UrlShortenRequest request = new UrlShortenRequest();
        request.setOriginalUrl("https://spring.io");

        mockMvc.perform(post("/api/urls")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.originalUrl").value("https://spring.io"))
                .andExpect(jsonPath("$.shortCode").isNotEmpty())
                .andExpect(jsonPath("$.shortUrl", containsString("/r/")))
                .andExpect(jsonPath("$.clickCount").value(0));
    }

    @Test
    void createUrl_shouldReturn400_whenOriginalUrlIsBlank() throws Exception {
        UrlShortenRequest request = new UrlShortenRequest();
        request.setOriginalUrl("");

        mockMvc.perform(post("/api/urls")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createUrl_shouldReturn400_whenOriginalUrlIsInvalid() throws Exception {
        UrlShortenRequest request = new UrlShortenRequest();
        request.setOriginalUrl("not-a-url");

        mockMvc.perform(post("/api/urls")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listUrls_shouldReturn200_withPagination() throws Exception {
        mockMvc.perform(get("/api/urls?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.pageSize").value(10));
    }
}