package com.example.Blog.Application.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection="blogs")
public class Blog {

    @Id
    public String id;

    public String authorId;

    @TextIndexed
    public String title;

    @TextIndexed
    public String content;

    public String summary;
    public String category;
    public List<String> tags;
    public String imageUrl;
    public BlogStatus status = BlogStatus.DRAFT;

    public long views=0;
    public Set<String> likedBy = new HashSet<>();
    public Set<String> bookmarkedBy= new HashSet<>();

    public LocalDateTime createdAt = LocalDateTime.now();
    public LocalDateTime updatedAt = LocalDateTime.now();
}
