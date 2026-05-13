package com.example.Blog.Application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
public class User {

    @Id
    public String id;

    public String name;

    @Indexed(unique=true)
    public String email;

    @JsonIgnore
    public String passwordHash;
    public String bio;
    public String avatarUrl;

    public Set<String> following=new HashSet<>();
    public LocalDateTime createdAt = LocalDateTime.now();
}
