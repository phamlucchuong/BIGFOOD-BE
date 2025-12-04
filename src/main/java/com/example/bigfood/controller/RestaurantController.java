package com.example.bigfood.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bigfood.dto.response.ApiResponse;
import com.example.bigfood.dto.response.RestaurantResponse;
import com.example.bigfood.entity.Restaurant;
import com.example.bigfood.service.RestaurantService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private RestaurantService restaurantService;



    // @PostMapping()
    // public ApiResponse<RestaurantResponse> postMethodName(@RequestBody String entity) {
    //     return ApiResponse.<RestaurantResponse>builder()
    //         .results(restaurantService.createNewRestaurant())
    //         .build();
    // }
    
}
