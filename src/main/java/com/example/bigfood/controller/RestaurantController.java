package com.example.bigfood.controller;

import java.io.IOException;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bigfood.dto.request.CreateRestaurantRequest;
import com.example.bigfood.dto.response.ApiResponse;
import com.example.bigfood.dto.response.RestaurantResponse;
import com.example.bigfood.service.RestaurantService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantController {
    RestaurantService restaurantService;

    @PostMapping
    public ApiResponse<RestaurantResponse> createRestaurant(
            @AuthenticationPrincipal Jwt jwt,
            @ModelAttribute CreateRestaurantRequest request)
            throws IOException {
        String userId = jwt.getSubject();
        return ApiResponse.<RestaurantResponse>builder()
                .results(restaurantService.createNewRestaurant(userId, request))
                .build();
    }

    @GetMapping
    public ApiResponse<RestaurantResponse> getRestaurantById(
            @AuthenticationPrincipal Jwt jwt)
            throws IOException {
        String userId = jwt.getSubject();
        return ApiResponse.<RestaurantResponse>builder()
                .results(restaurantService.getRestaurant(userId))
                .build();
    }

}
