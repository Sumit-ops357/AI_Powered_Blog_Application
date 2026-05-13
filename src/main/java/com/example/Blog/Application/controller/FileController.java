package com.example.Blog.Application.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

//User selects image (any files like:- PDFs,word ) in Next.js frontend.
//Frontend sends image to backend.
//Backend stores image in a folder
//Backend returns URL of uploaded image

@RestController
@RequestMapping("/api/files")
public class FileController {
    private static final long MAX_UPLOAD_BYTES = 5 * 1024 * 1024;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> upload(@RequestParam MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new BadRequestException("File is required");
        }
        if (file.getSize() > MAX_UPLOAD_BYTES) {
            throw new BadRequestException("File must be 5MB or smaller");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
            throw new BadRequestException("Only image uploads are allowed");
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() == null ? "" : file.getOriginalFilename()
        );
        String extension = extension(originalFilename);
        if (!isAllowedExtension(extension)) {
            throw new BadRequestException("Unsupported image type");
        }

        Path uploadRoot = Path.of(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadRoot);

        String filename = UUID.randomUUID() + extension;
        Path target = uploadRoot.resolve(filename).normalize();
        if (!target.startsWith(uploadRoot)) {
            throw new BadRequestException("Invalid upload path");
        }

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return Map.of("url", "http://localhost:8081/uploads/" + filename);
    }

    private String extension(String filename) {
        int dot = filename.lastIndexOf('.');
        if (dot < 0) {
            return "";
        }
        return filename.substring(dot).toLowerCase(Locale.ROOT);
    }

    private boolean isAllowedExtension(String extension) {
        return extension.equals(".jpg")
                || extension.equals(".jpeg")
                || extension.equals(".png")
                || extension.equals(".gif")
                || extension.equals(".webp");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private static class BadRequestException extends RuntimeException {
        BadRequestException(String message) {
            super(message);
        }
    }
}
