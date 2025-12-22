package com.example.bigfood.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bigfood.dto.request.RestaurantCategoryCreateRequest;
import com.example.bigfood.dto.response.ApiResponse;
import com.example.bigfood.dto.response.RestaurantCategoryResponse;
import com.example.bigfood.service.RestaurantCategoryService;

@RestController
@RequestMapping("/api/restaurant-categories")
public class RestaurantCategoryCotroller {
    @Autowired 
    RestaurantCategoryService restaurantCategoryService;

    @PostMapping
     public ApiResponse<RestaurantCategoryResponse> create(@RequestBody RestaurantCategoryCreateRequest request){
        ApiResponse<RestaurantCategoryResponse> response = new ApiResponse<>();
        response.setResults(restaurantCategoryService.create(request));
        return response;
    }
    
    @GetMapping
    public ApiResponse<List<RestaurantCategoryResponse>> getAll(){
        ApiResponse<List<RestaurantCategoryResponse>> response = new ApiResponse<>();
        response.setResults(restaurantCategoryService.getAll());
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<RestaurantCategoryResponse> getById(@PathVariable String id){
        ApiResponse<RestaurantCategoryResponse> response = new ApiResponse<>();
        response.setResults(restaurantCategoryService.getById(id));
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteById(@PathVariable String id){
        restaurantCategoryService.deleteById(id);
        return ApiResponse.<Void>builder().build();
    }
}
