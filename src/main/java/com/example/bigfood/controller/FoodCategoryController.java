package com.example.bigfood.controller;

import java.util.Set;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bigfood.dto.request.CreateFoodCategoryRequest;
import com.example.bigfood.dto.request.UpdateFoodCategoryRequest;
import com.example.bigfood.dto.response.ApiResponse;
import com.example.bigfood.dto.response.FoodCategoryResponse;
import com.example.bigfood.service.FoodCategoryService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/food-categories")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class FoodCategoryController {
    FoodCategoryService foodCategoryService;


    @PostMapping
    public ApiResponse<FoodCategoryResponse> createNewFoodCategory(
        @AuthenticationPrincipal Jwt jwt,
        @RequestBody CreateFoodCategoryRequest request) {
        String userId = jwt.getSubject();
        return ApiResponse.<FoodCategoryResponse>builder()
            .results(foodCategoryService.createFoodCategory(userId, request))
            .build();
    }

    @PatchMapping
    public ApiResponse<FoodCategoryResponse> updateFoodCategory(
        @AuthenticationPrincipal Jwt jwt,
        @RequestBody UpdateFoodCategoryRequest request) {
        String userId = jwt.getSubject();
        return ApiResponse.<FoodCategoryResponse>builder()
            .results(foodCategoryService.updateFoodCategory(userId, request))
            .build();
    }

    @GetMapping("/{restaurantId}/all")
    public ApiResponse<Set<FoodCategoryResponse>> getAllByUserId(@PathVariable String restaurantId) {
        return  ApiResponse.<Set<FoodCategoryResponse>>builder()
            .results(foodCategoryService.getAllFoodCategories(restaurantId))
            .build();
    }

    @DeleteMapping("/{categoryId}")
    public ApiResponse<Void> deleteFoodCategory(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable String categoryId) {
        String userId = jwt.getSubject();
        foodCategoryService.deleteFoodCategory(userId, categoryId);
        return ApiResponse.<Void>builder().message("Delete food category successfully!").build();
    }
}
