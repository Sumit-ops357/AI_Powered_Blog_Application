package com.example.Blog.Application.controller;

import com.example.Blog.Application.dto.Dtos;
import com.example.Blog.Application.service.AiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/summary")
    public Dtos.AiResponse summary(@RequestBody Dtos.AiRequest request) {
        return new Dtos.AiResponse(aiService.ask("Summarize this blog in 3 short lines:\n\n" + request.text()));
    }

    @PostMapping("/titles")
    public Dtos.AiResponse titles(@RequestBody Dtos.AiRequest request) {
        return new Dtos.AiResponse(aiService.ask("Generate 5 catchy blog titles for this article:\n\n" + request.text()));
    }

    @PostMapping("/tags")
    public Dtos.AiResponse tags(@RequestBody Dtos.AiRequest request) {
        return new Dtos.AiResponse(aiService.ask("Generate 8 SEO-friendly tags as comma-separated values:\n\n" + request.text()));
    }

    @PostMapping("/grammar")
    public Dtos.AiResponse grammar(@RequestBody Dtos.AiRequest request) {
        return new Dtos.AiResponse(aiService.ask("Fix grammar and spelling. Return only the corrected article:\n\n" + request.text()));
    }

    @PostMapping("/suggestions")
    public Dtos.AiResponse suggestions(@RequestBody Dtos.AiRequest request) {
        return new Dtos.AiResponse(aiService.ask("Suggest 3 useful paragraphs to continue this blog:\n\n" + request.text()));
    }
}
