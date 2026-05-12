package com.example.Blog.Application.controller;

import com.example.Blog.Application.dto.Dtos;
import com.example.Blog.Application.model.User;
import com.example.Blog.Application.repository.UserRepository;
import com.example.Blog.Application.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

     //Final keyword is used to restrict the modification
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,PasswordEncoder passwordEncoder,JwtService jwtService)
    {
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtService=jwtService;
    }

    @PostMapping("/signup")
    public Dtos.AuthResponse signup(@RequestBody Dtos.SignupRequest request)
    {
        if(userRepository.existsByEmail(request.email()))
        {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.name = request.name();
        user.email=request.email();
        user.passwordHash=passwordEncoder.encode(request.password());

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved.id,saved.email);

        return new Dtos.AuthResponse(token,saved.id,saved.name,saved.email);
    }

    @PostMapping("/login")
    public Dtos.AuthResponse login(@RequestBody Dtos.LoginRequest request)
    {
        User user = userRepository.findByEmail(request.email()).
                orElseThrow(() -> new RuntimeException("User doesn't Exist"));

        if(!passwordEncoder.matches(request.password(), user.passwordHash))
        {
            throw new RuntimeException("Invalid Credentials");
        }

        String token = jwtService.generateToken(user.id,user.email);

        return new Dtos.AuthResponse(token,user.id,user.name,user.email);
    }
}
