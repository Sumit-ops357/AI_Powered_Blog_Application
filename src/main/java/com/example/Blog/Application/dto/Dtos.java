package com.example.Blog.Application.dto;


import com.example.Blog.Application.model.BlogStatus;
import com.example.Blog.Application.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class Dtos {

    public record SignupRequest(String name,String email,String password) {}
    public record LoginRequest(String email,String password) {}
    public record AuthResponse(String token,String userId,String name,String email) {}
    public record UserResponse(
            String id,
            String name,
            String email,
            String bio,
            String avatarUrl,
            Set<String> following,
            LocalDateTime createdAt
    ) {
        public static UserResponse from(User user) {
            return new UserResponse(
                    user.id,
                    user.name,
                    user.email,
                    user.bio,
                    user.avatarUrl,
                    user.following == null ? Set.of() : Set.copyOf(user.following),
                    user.createdAt
            );
        }
    }

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
