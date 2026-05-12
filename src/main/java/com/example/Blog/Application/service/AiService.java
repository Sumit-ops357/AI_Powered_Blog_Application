package com.example.Blog.Application.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${app.gemini.api-key}")
    private String apiKey;

    @Value("${app.gemini.model}")
    private String model;

    //We are creating restClient object which is used to send http requests to the Gemini API server
    private final RestClient restClient =
            RestClient.create("https://generativelanguage.googleapis.com");

    public String ask(String prompt) {

        if(apiKey==null || apiKey.isBlank())
        {
            return "AI is not configured, Set Gemini API key and restart the backend server";
        }

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful writing assistant for bloggers."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
        );

        JsonNode response = restClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .body(JsonNode.class);

        return response
                .path("choices")
                .path(0)
                .path("message")
                .path("content")
                .asText();
    }
}
