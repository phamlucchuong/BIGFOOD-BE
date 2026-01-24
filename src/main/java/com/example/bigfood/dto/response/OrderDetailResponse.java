package com.example.bigfood.dto.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderDetailResponse {
    String id;
    String status;
    String createdAt;
    String updatedAt;
    String deliveryAddress;
    double deliveryDistance;
    double deliveryFee;
    double totalAmount;
    String paymentMethod;
    String notes;
    String cancellReason;
    String rejectReason;
    int numberDishes ;
    Set<OrderFoodDetailResponse> orderDetails; 
    InfoUserOrderResponse user; 
}
