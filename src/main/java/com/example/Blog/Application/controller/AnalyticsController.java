package com.example.Blog.Application.controller;

import com.example.Blog.Application.service.AnalyticsService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService)
    {
        this.analyticsService=analyticsService;
    }

    @GetMapping("/me")
    public Map<String, Object> myAnalytics(Authentication authentication) {
        return analyticsService.userAnalytics(authentication.getName());
    }
}
