package com.example.Blog.Application.controller;

import com.example.Blog.Application.dto.Dtos;
import com.example.Blog.Application.model.Blog;
import com.example.Blog.Application.model.BlogStatus;
import com.example.Blog.Application.model.User;
import com.example.Blog.Application.repository.BlogRepository;
import com.example.Blog.Application.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/social")
public class SocialController {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    public SocialController(BlogRepository blogRepository,UserRepository userRepository)
    {
        this.blogRepository=blogRepository;
        this.userRepository=userRepository;
    }

    @PostMapping("/blogs/{blogId}/like")
    public Blog toggleLike(@PathVariable String blogId, Authentication authentication)
    {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        if (blog.status != BlogStatus.PUBLISHED) {
            throw new RuntimeException("Blog not found");
        }

        String userId = authentication.getName();

        if(blog.likedBy.contains(userId)) {
            blog.likedBy.remove(userId);
        }
        else{
            blog.likedBy.add(userId);
        }

        return blogRepository.save(blog);
    }

    @PostMapping("/blogs/{blogId}/bookmark")
    public Blog toggleBookmark(@PathVariable String blogId, Authentication authentication) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        if (blog.status != BlogStatus.PUBLISHED) {
            throw new RuntimeException("Blog not found");
        }

        String userId = authentication.getName();

        if (blog.bookmarkedBy.contains(userId)) {
            blog.bookmarkedBy.remove(userId);
        } else {
            blog.bookmarkedBy.add(userId);
        }

        return blogRepository.save(blog);
    }

    @PostMapping("/authors/{authorId}/follow")
    public Dtos.UserResponse toggleFollow(@PathVariable String authorId, Authentication authentication) {
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.following.contains(authorId)) {
            user.following.remove(authorId);
        } else {
            user.following.add(authorId);
        }

        return Dtos.UserResponse.from(userRepository.save(user));
    }

    @GetMapping("/profiles/{id}")
    public Dtos.UserResponse profile(@PathVariable String id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return Dtos.UserResponse.from(user);
    }
}
