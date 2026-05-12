package com.example.Blog.Application.controller;

import com.example.Blog.Application.dto.Dtos;
import com.example.Blog.Application.model.Comment;
import com.example.Blog.Application.model.User;
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

    public CommentController(CommentRepository commentRepository,UserRepository userRepository)
    {
        this.commentRepository=commentRepository;
        this.userRepository=userRepository;
    }

    @GetMapping("/{blogId}")
    public List<Comment> getComments(@PathVariable String blogId)
    {
        return commentRepository.findByBlogIdOrderByCreatedAtDesc(blogId);
    }

    @PostMapping("/{blogId}")
    public Comment addComment(@PathVariable String blogId, @RequestBody Dtos.CommentRequest request, Authentication authentication)
    {
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
