package com.example.bigfood.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bigfood.dto.request.CreateFoodCategoryRequest;
import com.example.bigfood.dto.request.CreateFoodRequest;
import com.example.bigfood.dto.request.CreateRestaurantRequest;
import com.example.bigfood.dto.request.UpdateFoodCategoryRequest;
import com.example.bigfood.dto.response.ApiResponse;
import com.example.bigfood.dto.response.FoodCategoryResponse;
import com.example.bigfood.dto.response.FoodResponse;
import com.example.bigfood.dto.response.RestaurantDetailResponse;
import com.example.bigfood.dto.response.RestaurantResponse;
import com.example.bigfood.service.RestaurantService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestBody;






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

    @GetMapping("/detail")
    public ApiResponse<?> getRestaurantDetail(
        @AuthenticationPrincipal Jwt jwt) 
        throws IOException {
        String userId = jwt.getSubject();
        return ApiResponse.<RestaurantDetailResponse>builder()
            .results(restaurantService.getRestaurantDetail(userId))
            .build();
    }
    

    //#region handle cate


    @PostMapping("/food-category")
    public ApiResponse<FoodCategoryResponse> createNewFoodCategory(
        @AuthenticationPrincipal Jwt jwt,
        @RequestBody CreateFoodCategoryRequest request) {
        String userId = jwt.getSubject();
        return ApiResponse.<FoodCategoryResponse>builder()
            .results(restaurantService.createFoodCategory(userId, request))
            .build();
    }

    @PatchMapping("/food-category")
    public ApiResponse<FoodCategoryResponse> updateFoodCategory(
        @AuthenticationPrincipal Jwt jwt,
        @RequestBody UpdateFoodCategoryRequest request) {
        String userId = jwt.getSubject();
        return ApiResponse.<FoodCategoryResponse>builder()
            .results(restaurantService.updateFoodCategory(userId, request))
            .build();
    }

    @GetMapping("/{categoryId}/food-category/all")
    public ApiResponse<Set<FoodCategoryResponse>> getAllByUserId(@PathVariable String categoryId) {
        return  ApiResponse.<Set<FoodCategoryResponse>>builder()
            .results(restaurantService.getAllFoodCategories(categoryId))
            .build();
    }

    @DeleteMapping("/food-category/{categoryId}")
    public ApiResponse<Void> deleteFoodCategory(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable String categoryId) {
        String userId = jwt.getSubject();
        restaurantService.deleteFoodCategory(userId, categoryId);
        return ApiResponse.<Void>builder().message("Delete food category successfully!").build();
    }


    //#endregion


    //#region handle food


    @PostMapping("/{categoryId}/food")
    public ApiResponse<FoodResponse> createFood(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable String categoryId,
        @ModelAttribute CreateFoodRequest request) 
        throws IOException {
        String userId = jwt.getSubject();
        return ApiResponse.<FoodResponse>builder()
            .results(restaurantService.createFood(userId, categoryId, request))
            .build();
    }
    
    @GetMapping("/food/all")
    public ApiResponse<List<FoodResponse>> getAllFood(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return ApiResponse.<List<FoodResponse>>builder()
            .results(restaurantService.getAllFood(userId))
            .build();
    }


    @DeleteMapping("/food/{foodId}")
    public ApiResponse<Void> deleteFood(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable String foodId)
        throws IOException {
        String userId = jwt.getSubject();
        restaurantService.deleteFood(userId, foodId);
        return ApiResponse.<Void>builder().message("Delete food successfully!").build();
    }
    
    //#endregion
}
