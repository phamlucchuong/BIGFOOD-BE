package com.example.bigfood.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.bigfood.dto.request.CreateOrderRequest;
import com.example.bigfood.dto.response.ApiResponse;
import com.example.bigfood.dto.response.OrderResponse;
import com.example.bigfood.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    
    @PostMapping
    public ApiResponse<OrderResponse> postMethodName(
        @AuthenticationPrincipal Jwt jwt,
        @RequestBody CreateOrderRequest request) {
        String userId = jwt.getSubject();
        return ApiResponse.<OrderResponse>builder()
            .results(orderService.createOrder(userId, request))
            .message("Order created successfully for user: " + userId)
            .build();
    }


    @GetMapping("/all")
    @PostAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<OrderResponse>> getAllOrders() {
        return ApiResponse.<List<OrderResponse>>builder()
            .results(orderService.getAllOrders())
            .message("Fetched all orders")
            .build();
    }

    @GetMapping("/{userId}/all")
    @PostAuthorize("hasRole('ADMIN') or principal.subject == #userId")
    public ApiResponse<List<OrderResponse>> getAllOrdersById(@AuthenticationPrincipal Jwt jwt, @PathVariable String userId) {
        return ApiResponse.<List<OrderResponse>>builder()
            .results(orderService.getAllOrdersByUserId(userId))
            .message("Fetched all orders for user: " + userId)
            .build();
    }
    
    
}
