package com.example.Blog.Application.dto;


import com.example.Blog.Application.model.BlogStatus;

import java.util.List;

public class Dtos {

    public record SignupRequest(String name,String email,String password) {}
    public record LoginRequest(String email,String password) {}
    public record AuthResponse(String token,String userId,String name,String email) {}

    public record BlogRequest(
            String title,
            String content,
            String summary,
            String category,
            List<String> tags,
            String imageUrl,
            BlogStatus blogStatus
    ) {}

    public record CommentRequest(String message) {}
    public record AiRequest(String text) {}
    public record AiResponse(String result) {}
}
