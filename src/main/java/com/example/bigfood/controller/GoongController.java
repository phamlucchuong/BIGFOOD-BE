package com.example.bigfood.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinary.Api;
import com.example.bigfood.dto.response.ApiResponse;
import com.example.bigfood.service.GoongService;
import com.example.bigfood.service.GoongService.GeocodingResponse;
import com.example.bigfood.service.GoongService.GoongLocation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/goong")
public class GoongController {
    private final GoongService goongService;

    public GoongController(GoongService goongService) {
        this.goongService = goongService;
    }

    @PostMapping("/geocoding")
    public ApiResponse<?> geocoding(@RequestBody String address) {
        return ApiResponse.builder()
            .results(goongService.getGeocoding(address))
            .build();
    }


    @PostMapping("/reverse-geocoding")
    public ApiResponse<?> reverseGeocoding(@RequestBody GoongLocation geocoding) {
        return ApiResponse.builder()
            .results(goongService.getReverseGeocoding(geocoding))
            .build();
    }
    
}
