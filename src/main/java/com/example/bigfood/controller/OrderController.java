package com.example.bigfood.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.bigfood.dto.request.CreateOrderRequest;
import com.example.bigfood.dto.request.UpdateOrderStatusRequest;
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
import org.springframework.web.bind.annotation.PatchMapping;
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

    @GetMapping("/user/{userId}/all")
    @PostAuthorize("hasRole('ADMIN') or principal.subject == #userId")
    public ApiResponse<List<OrderResponse>> getAllOrdersByUserId(@AuthenticationPrincipal Jwt jwt, @PathVariable String userId) {
        return ApiResponse.<List<OrderResponse>>builder()
            .results(orderService.getAllOrdersByUserId(userId))
            .message("Fetched all orders for user: " + userId)
            .build();
    }

    @GetMapping("/restaurant/{restaurantId}/all")
    @PostAuthorize("hasRole('ADMIN') or hasRole('RESTAURANT') or principal.subject == #restaurantId")
    public ApiResponse<List<OrderResponse>> getAllOrdersByRestaurantId(@AuthenticationPrincipal Jwt jwt, @PathVariable String restaurantId) {
        return ApiResponse.<List<OrderResponse>>builder()
            .results(orderService.getAllOrdersByRestaurantId(restaurantId))
            .message("Fetched all orders for restaurant: " + restaurantId)
            .build();
    }
    
    @PatchMapping("/{orderId}/status")
    public ApiResponse<OrderResponse> updateOrderStatus(
        @PathVariable String orderId,
        @RequestBody UpdateOrderStatusRequest request) {
        return ApiResponse.<OrderResponse>builder()
            .results(orderService.updateOrderStatus(orderId, request))
            .message("Order status updated successfully for order: " + orderId)
            .build();
    }
}
