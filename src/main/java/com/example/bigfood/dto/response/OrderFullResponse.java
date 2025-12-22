package com.example.bigfood.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderFullResponse {
    String id;
    String restaurantName;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String deliveryAddress;
    double deliveryDistance;
    double deliveryFee;
    double totalAmount;
    String paymentMethod;
    String notes;
    String cancelReason;
    String rejectReason;
    Set<OrderItemResponse> orderDetails;
    String userId;
    ReviewShortResponse review;
}
