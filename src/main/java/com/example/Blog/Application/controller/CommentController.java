package com.example.Blog.Application.controller;

import com.example.Blog.Application.dto.Dtos;
import com.example.Blog.Application.model.Blog;
import com.example.Blog.Application.model.BlogStatus;
import com.example.Blog.Application.model.Comment;
import com.example.Blog.Application.model.User;
import com.example.Blog.Application.repository.BlogRepository;
import com.example.Blog.Application.repository.CommentRepository;
import com.example.Blog.Application.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;

    public CommentController(CommentRepository commentRepository,UserRepository userRepository,BlogRepository blogRepository)
    {
        this.commentRepository=commentRepository;
        this.userRepository=userRepository;
        this.blogRepository=blogRepository;
    }

    @GetMapping("/{blogId}")
    public List<Comment> getComments(@PathVariable String blogId)
    {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        if (blog.status != BlogStatus.PUBLISHED) {
            throw new RuntimeException("Blog not found");
        }

        return commentRepository.findByBlogIdOrderByCreatedAtDesc(blogId);
    }

    @PostMapping("/{blogId}")
    public Comment addComment(@PathVariable String blogId, @RequestBody Dtos.CommentRequest request, Authentication authentication)
    {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        if (blog.status != BlogStatus.PUBLISHED) {
            throw new RuntimeException("Cannot comment on an unpublished blog");
        }

        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.blogId = blogId;
        comment.authorId = user.id;
        comment.authorName=user.name;
        comment.message = request.message();

        return commentRepository.save(comment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable String id, Authentication authentication)
    {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if(!comment.authorId.equals(authentication.getName()))
        {
            throw new RuntimeException("You can delete only your own comment");
        }

        commentRepository.deleteById(id);
    }
}
