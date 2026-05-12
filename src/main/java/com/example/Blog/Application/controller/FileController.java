package com.example.Blog.Application.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

//User selects image (any files like:- PDFs,word ) in Next.js frontend.
//Frontend sends image to backend.
//Backend stores image in a folder
//Backend returns URL of uploaded image

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> upload(@RequestParam MultipartFile file) throws Exception {
        Files.createDirectories(Path.of(uploadDir));

        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path target = Path.of(uploadDir, filename);

        Files.copy(file.getInputStream(), target);

        return Map.of("url", "http://localhost:8080/uploads/" + filename);
    }
}
