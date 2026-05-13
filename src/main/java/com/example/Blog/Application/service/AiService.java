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
                "systemInstruction", Map.of(
                        "parts", List.of(
                                Map.of("text", "You are a helpful writing assistant for bloggers.")
                        )
                ),
                "contents", List.of(
                        Map.of(
                                "role", "user",
                                "parts", List.of(Map.of("text", prompt))
                        )
                ),
                "generationConfig", Map.of(
                        "temperature", 0.7
                )
        );

        JsonNode response = restClient.post()
                .uri("/v1beta/models/{model}:generateContent", normalizeModel(model))
                .header("x-goog-api-key", apiKey)
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .body(JsonNode.class);

        return response
                .path("candidates")
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text")
                .asText();
    }

    private String normalizeModel(String model) {
        if (model == null || model.isBlank()) {
            return "gemini-2.5-flash";
        }

        return model.startsWith("models/") ? model.substring("models/".length()) : model;
    }
}
