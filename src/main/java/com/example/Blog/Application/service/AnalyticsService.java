package com.example.Blog.Application.service;

import com.example.Blog.Application.model.Blog;
import com.example.Blog.Application.repository.BlogRepository;
import com.example.Blog.Application.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyticsService {

    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;

    public AnalyticsService(BlogRepository blogRepository,CommentRepository commentRepository)
    {
        this.blogRepository=blogRepository;
        this.commentRepository=commentRepository;
    }

    public Map<String, Object> userAnalytics(String userId) {
        var blogs = blogRepository.findByAuthorId(userId);

        long totalViews = blogs.stream().mapToLong(blog -> blog.views).sum();
        long totalLikes = blogs.stream().mapToLong(blog -> blog.likedBy.size()).sum();
        long totalComments = commentRepository.countByAuthorId(userId);

        Blog popularBlog = blogs.stream()
                .max(Comparator.comparingLong(blog -> blog.views + blog.likedBy.size()))
                .orElse(null);

        Map<String, Object> result = new HashMap<>();
        result.put("totalBlogs", blogs.size());
        result.put("totalViews", totalViews);
        result.put("totalLikes", totalLikes);
        result.put("commentsWritten", totalComments);
        result.put("popularBlog", popularBlog);

        return result;
    }
}
