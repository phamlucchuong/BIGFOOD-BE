package com.example.bigfood.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.bigfood.dto.request.UserCreateRequest;
import com.example.bigfood.dto.request.UserUpdateRequest;
import com.example.bigfood.dto.response.UserResponse;
import com.example.bigfood.dto.response.ApiResponse;

import com.example.bigfood.service.UserService;

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
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    
    @GetMapping("/verify-email/{email}")
    public ApiResponse<Boolean> verifyEmail(@PathVariable String email){
        return ApiResponse.<Boolean>builder()
        .results(userService.verifyEmail(email))
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

    @PutMapping("/{email}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String email , @RequestBody UserUpdateRequest request){
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setResults(userService.updateUser(email, request));
        return response;
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable String id){
        ApiResponse<Void> response = new ApiResponse<>();
        userService.deleteUser(id);
        return response;
    }
    
}
