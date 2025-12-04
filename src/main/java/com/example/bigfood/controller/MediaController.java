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
    private final UserService userService; // Dịch vụ để lưu URL vào DB

    public MediaController(CloudinaryService cloudinaryService, UserService userService) {
        this.cloudinaryService = cloudinaryService;
        this.userService = userService;
    }

    @PostMapping("/profile-picture")
    public ApiResponse<String> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.<String>builder()
                    .message("Tệp tin không được để trống.")
                    .build();
        }
        
        try {
            // 1. Tải lên Cloudinary
            String imageUrl = cloudinaryService.uploadFile(file, "images/profile_pictures");
            
            // 2. Lưu URL vào Database
            // user.setProfilePictureUrl(imageUrl);
            // userService.save(user);

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
