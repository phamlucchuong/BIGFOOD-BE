package com.example.bigfood.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.bigfood.dto.response.ApiResponse;
import com.example.bigfood.service.CloudinaryService;
import com.example.bigfood.service.UserService;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final CloudinaryService cloudinaryService;

    public MediaController(CloudinaryService cloudinaryService, UserService userService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping
    public ApiResponse<String> uploadProfilePicture(
        @RequestParam("folder") String folder,
        @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.<String>builder()
                    .message("Tệp tin không được để trống.")
                    .build();
        }
        
        try {
            // 1. Tải lên Cloudinary
            String imageUrl = cloudinaryService.uploadFile(file, "images/" + folder);
            
            return ApiResponse.<String>builder()
                    .message("Tải lên thành công.")
                    .results(imageUrl)
                    .build();
            
        } catch (IOException e) {
            return ApiResponse.<String>builder()
                    .message("Lỗi tải lên: " + e.getMessage())
                    .build();
        }
    }
}
