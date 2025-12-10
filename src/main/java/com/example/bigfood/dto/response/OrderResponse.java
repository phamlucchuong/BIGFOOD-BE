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
public class OrderResponse {
    String orderId;
    String status;
    String createdAt;
    String updatedAt;
    String deliveryAddress;
    double deliveryLatitude;
    double deliveryLongitude;
    double deliveryFee;
    double totalAmount;
    String paymentMethod;
    String notes;
    String cancellReason;
    String rejectReason;
    Set<OrderDetailResponse> orderDetails;
}
