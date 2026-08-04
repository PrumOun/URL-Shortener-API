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
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RedirectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void redirect_shouldReturn302_whenShortCodeExists() throws Exception {
        // Arrange — create a URL first, extract its shortCode
        UrlShortenRequest request = new UrlShortenRequest();
        request.setOriginalUrl("https://spring.io");

        MvcResult createResult = mockMvc.perform(post("/api/urls")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode json = objectMapper.readTree(createResult.getResponse().getContentAsString());
        String shortCode = json.get("shortCode").asText();

        // Act & Assert
        mockMvc.perform(get("/r/" + shortCode))
                .andExpect(status().isFound()) // 302
                .andExpect(header().string("Location", "https://spring.io"));
    }

    @Test
    void redirect_shouldReturn404_whenShortCodeDoesNotExist() throws Exception {
        mockMvc.perform(get("/r/doesnotexist"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
