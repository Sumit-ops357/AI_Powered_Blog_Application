package com.example.Blog.Application.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comments")
public class Comment {

    @Id
    public String id;

    public String blogId;
    public String authorId;
    public String authorName;
    public String message;

    public LocalDateTime createdAt = LocalDateTime.now();
}
