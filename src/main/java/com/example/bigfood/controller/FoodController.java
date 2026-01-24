package com.example.bigfood.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bigfood.dto.request.CreateFoodRequest;
import com.example.bigfood.dto.request.UpdateFoodRequest;
import com.example.bigfood.dto.response.ApiResponse;
import com.example.bigfood.dto.response.FoodResponse;
import com.example.bigfood.dto.response.PageResponse;
import com.example.bigfood.service.FoodService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class FoodController {
    FoodService foodService;

    @PostMapping("/{categoryId}")
    public ApiResponse<FoodResponse> createFood(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable String categoryId,
        @ModelAttribute CreateFoodRequest request) 
        throws IOException {
        String userId = jwt.getSubject();
        return ApiResponse.<FoodResponse>builder()
            .results(foodService.createFood(userId, categoryId, request))
            .build();
    }
    
    @GetMapping("/all/page/{page}")
    public ApiResponse<PageResponse<FoodResponse>> getAllFood(@AuthenticationPrincipal Jwt jwt , @PathVariable("page") Integer page)  {
        String userId = jwt.getSubject();
        return ApiResponse.<PageResponse<FoodResponse>>builder()
            .results(foodService.getAllByUserId(userId ,  page != null ? page : 0))
            .build();
    }
 
    @GetMapping("/list")
    public ApiResponse<List<FoodResponse>> getByCategoryId(
        @AuthenticationPrincipal Jwt jwt , 
        @RequestParam(required = false) String categoryId) {
        String userId = jwt.getSubject();
        return ApiResponse.<List<FoodResponse>>builder()
            .results(foodService.listFoodByCategoryId(userId , categoryId))
            .build();
    }
     @GetMapping("/list/best-sell")
    public ApiResponse<List<FoodResponse>> getFoodTop5BestSell(
        @AuthenticationPrincipal Jwt jwt ) {
        String userId = jwt.getSubject();
        return ApiResponse.<List<FoodResponse>>builder()
            .results(foodService.getTop5BestSellingFoods(userId ))
            .build();
    }
     @GetMapping("/list/least-sell")
    public ApiResponse<List<FoodResponse>> getFoodTop5LeastSell(
        @AuthenticationPrincipal Jwt jwt ) {
        String userId = jwt.getSubject();
        return ApiResponse.<List<FoodResponse>>builder()
            .results(foodService.getTop5LeastSellingFoods(userId))
            .build();
    }



    @PutMapping("/update")
        public ApiResponse<FoodResponse> updateFoods(
            @AuthenticationPrincipal Jwt jwt,
            @ModelAttribute UpdateFoodRequest request) 
            throws IOException {
            String userId = jwt.getSubject();
            return ApiResponse.<FoodResponse>builder()
                .results(foodService.updateFood(userId ,request))
                .build();
        }

    @DeleteMapping("/{foodId}")
    public ApiResponse<Void> deleteFood(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable String foodId)
        throws IOException {
        String userId = jwt.getSubject();
        foodService.deleteFood(userId, foodId);
        return ApiResponse.<Void>builder().message("Delete food successfully!").build();
    }
}
