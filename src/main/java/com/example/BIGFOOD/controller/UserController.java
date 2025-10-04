package com.example.BIGFOOD.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.BIGFOOD.dto.request.UserCreateRequest;
import com.example.BIGFOOD.dto.request.UserUpdateRequest;
import com.example.BIGFOOD.dto.request.VerifyEmailRequest;
import com.example.BIGFOOD.dto.response.UserResponse;
import com.example.BIGFOOD.dto.response.ApiResponse;

import com.example.BIGFOOD.service.UserService;

import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/verify-email")
    public ApiResponse<Boolean> verifyEmail(@RequestBody VerifyEmailRequest request){
        return ApiResponse.<Boolean>builder()
        .results(userService.verifyEmail(request))
        .build();
    }

    @PostMapping
    public ApiResponse<UserResponse> create(@RequestBody UserCreateRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResults(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUser(){
        ApiResponse<List<UserResponse>> response = new ApiResponse<>();
        response.setResults(userService.getAllUser());
        return response;
    }

    @PutMapping("/{username}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String username , @RequestBody UserUpdateRequest request){
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setResults(userService.updateUser(username, request));
        return response;
    }
    
    @DeleteMapping("/{username}")
    public ApiResponse<Void> deleteUser(@PathVariable String username){
        ApiResponse<Void> response = new ApiResponse<>();
        userService.deleteUser(username);
        return response;
    }
    
}
