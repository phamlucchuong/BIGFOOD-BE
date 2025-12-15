package com.example.bigfood.controller;

import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bigfood.dto.request.CreateOrderRequest;
import com.example.bigfood.dto.request.UpdateOrderStatusRequest;
import com.example.bigfood.dto.response.ApiResponse;
import com.example.bigfood.dto.response.OrderDetailResponse;
import com.example.bigfood.dto.response.OrderFullResponse;
import com.example.bigfood.dto.response.OrderResponse;
import com.example.bigfood.dto.response.OrderShortResponse;
import com.example.bigfood.service.OrderService;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;



@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    
    @PostMapping
    public ApiResponse<OrderFullResponse> postMethodName(
        @AuthenticationPrincipal Jwt jwt,
        @RequestBody CreateOrderRequest request) {
        String userId = jwt.getSubject();
        return ApiResponse.<OrderFullResponse>builder()
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

    @GetMapping("/{id}")
    public ApiResponse<OrderFullResponse> getOrderById(@PathVariable String id) {
        return ApiResponse.<OrderFullResponse>builder()
            .results(orderService.getOrderByOrderId(id))
            .message("Fetched order with id: " + id)
            .build();
    }

    @GetMapping("/user/{userId}/all")
    @PostAuthorize("hasRole('ADMIN')")
    public ApiResponse<Set<OrderShortResponse>> getAllOrdersByUserId( @PathVariable String userId) {
        return ApiResponse.<Set<OrderShortResponse>>builder()
            .results(orderService.getAllOrdersByUserId(userId))
            .message("Fetched all orders for user: " + userId)
            .build();
    }


    /**
     * hàm lấy danh sách đơn hàng của user bằng jwt
     *
     * @param jwt token đăng nhập
     * @param page số trang
     * @return danh sách đơn hàng theo phân trang
     */
    @GetMapping("/user/all")
    // @PostAuthorize("hasRole('USER')")
    public ApiResponse<Set<OrderShortResponse>> getAllOrdersByUserId(
        @AuthenticationPrincipal Jwt jwt,
        @PathParam("page") Integer page) {
        String userId = jwt.getSubject();
        return ApiResponse.<Set<OrderShortResponse>>builder()
            .results(orderService.getAllOrdersByUserId(userId))
            .message("Fetched all orders for user: " + userId)
            .build();
    }

    @GetMapping("/restaurant/{restaurantId}/all")
    @PostAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<OrderResponse>> getAllOrdersByRestaurantId(@PathVariable String restaurantId) {
        return ApiResponse.<List<OrderResponse>>builder()
            .results(orderService.getAllOrdersByRestaurantId(restaurantId))
            .message("Fetched all orders for restaurant: " + restaurantId)
            .build();
    }

    @GetMapping("/restaurant/all")
    // @PostAuthorize("hasRole('RESTAURANT')")
    public ApiResponse<List<OrderResponse>> getAllOrdersByRestaurantId(@AuthenticationPrincipal Jwt jwt) {
        String restaurantId = jwt.getSubject();
        return ApiResponse.<List<OrderResponse>>builder()
            .results(orderService.getAllOrdersByRestaurantId(restaurantId))
            .message("Fetched all orders for restaurant: " + restaurantId)
            .build();
    }

    
    @GetMapping("/restaurant")
    // @PostAuthorize("hasRole('RESTAURANT')")
    public ApiResponse<List<OrderResponse>> getOrdersRestaurantByStatus(@AuthenticationPrincipal Jwt jwt,
                                         @PathParam("status") String status) {
        String restaurantId = jwt.getSubject();
        return ApiResponse.<List<OrderResponse>>builder()
            .results(orderService.getLoadStatusFilter(restaurantId , status))
            .message("Fetched all orders for restaurant: " + restaurantId)
            .build();
    }

    @GetMapping("/restaurant/detail/{orderId}")
    //  @PostAuthorize("hasRole('RESTAURANT')")
    public ApiResponse<OrderDetailResponse> getOrdersDetailByOrderId(@PathVariable String orderId) {
        return ApiResponse.<OrderDetailResponse>builder()
            .results(orderService.getOrderDetailByOrderId(orderId))
            .message("Orders deatil for restaurant: " + orderId)
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
