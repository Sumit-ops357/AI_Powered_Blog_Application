package com.example.Blog.Application.repository;

import com.example.Blog.Application.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByBlogIdOrderByCreatedAtDesc(String blogId);
    long countByAuthorId(String authorId);
}
