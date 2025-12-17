package com.example.bigfood.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bigfood.dto.request.ApproveRestaurantRequest;
import com.example.bigfood.dto.request.CreateRestaurantRequest;
import com.example.bigfood.dto.request.UpdateRestaurantRequest;
import com.example.bigfood.dto.response.ApiResponse;
import com.example.bigfood.dto.response.RestaurantProfileResponse;
import com.example.bigfood.dto.response.RestaurantResponse;
import com.example.bigfood.dto.response.RestaurantTagResponse;
import com.example.bigfood.dto.request.IDRequest;
import com.example.bigfood.dto.response.RestaurantDetailResponse;
import com.example.bigfood.dto.response.RestaurantsResponseSet;
import com.example.bigfood.dto.response.RestaurantFullResponse;
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
    public ApiResponse<RestaurantFullResponse> createRestaurant(
            @AuthenticationPrincipal Jwt jwt,
            @ModelAttribute CreateRestaurantRequest request)
            throws IOException {
        String userId = jwt.getSubject();
        return ApiResponse.<RestaurantFullResponse>builder()
                .results(restaurantService.createNewRestaurant(userId, request))
                .build();
    }

    @GetMapping("/myInfo")
    @PreAuthorize("hasRole('RESTAURANT') and returnedObject.userId == principal.subject")
    public ApiResponse<RestaurantFullResponse> getRestaurantById(
            @AuthenticationPrincipal Jwt jwt)
            throws IOException {
        String userId = jwt.getSubject();
        return ApiResponse.<RestaurantFullResponse>builder()
                .results(restaurantService.getRestaurant(userId))
                .build();
    }

    @GetMapping("/detail")
    public ApiResponse<RestaurantProfileResponse> getRestaurantDetail(
            @AuthenticationPrincipal Jwt jwt)
            throws IOException {
        String userId = jwt.getSubject();
        return ApiResponse.<RestaurantProfileResponse>builder()
                .results(restaurantService.getRestaurantDetail(userId))
                .build();
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> updateRestaurant(
            @AuthenticationPrincipal Jwt jwt,
            @ModelAttribute UpdateRestaurantRequest request)
            throws IOException {
        String userId = jwt.getSubject();
        return ApiResponse.<RestaurantProfileResponse>builder()
                .results(restaurantService.updateRestaurant(userId, request))
                .build();
    }

    @PostMapping("/detail")
    public ApiResponse<RestaurantDetailResponse> getRestaurantByRestaurantId(@RequestBody IDRequest restaurantId) {
        return ApiResponse.<RestaurantDetailResponse>builder()
                .results(restaurantService.getRestaurantByRestaurantId(restaurantId.getId()))
                .build();
    }

    @GetMapping("/request")
    @PostAuthorize("hasRole('ADMIN')")
    public ApiResponse<RestaurantsResponseSet<RestaurantTagResponse>> getRestaurantSet(
            @RequestParam(required = false) Integer page) {
        return ApiResponse.<RestaurantsResponseSet<RestaurantTagResponse>>builder()
                .results(restaurantService.getRestaurantRequestSet(page != null ? page : 0))
                .build();
    }

    @GetMapping
    public ApiResponse<RestaurantsResponseSet<RestaurantResponse>> getRestaurantSet(
            @RequestParam(name = "lng", required = false) Double longitude,
            @RequestParam(name = "lat", required = false) Double latitude,
            @RequestParam(name = "categoryId", required = false) String categoryId,
            @RequestParam(name = "searchText", required = false) String searchText,
            @RequestParam(required = false) Integer page) {
        return ApiResponse.<RestaurantsResponseSet<RestaurantResponse>>builder()
                .results(restaurantService.getRestaurantSet(longitude, latitude, categoryId, searchText,
                        page != null ? page : 0))
                .build();
    }

    @PatchMapping("/request/approve")
    public ApiResponse<Void> approveRestaurantRequest(
            @RequestBody ApproveRestaurantRequest request) {
        restaurantService.approveRestaurantRequest(request.getRestaurantId(), request.isApproved());
        return ApiResponse.<Void>builder().message("Hoan tat yeu cau xet duyet").build();
    }
}
