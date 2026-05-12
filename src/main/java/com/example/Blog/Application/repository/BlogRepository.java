package com.example.Blog.Application.repository;

import com.example.Blog.Application.model.Blog;
import com.example.Blog.Application.model.BlogStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BlogRepository extends MongoRepository<Blog, String> {

    List<Blog> findByStatus(BlogStatus status);
    List<Blog> findByAuthorId(String authorId);
    List<Blog> findByCategoryIgnoreCaseAndStatus(String category,BlogStatus status);
    List<Blog> findByTagsContainingAndStatus(String tag, BlogStatus status);
    List<Blog> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);
}
