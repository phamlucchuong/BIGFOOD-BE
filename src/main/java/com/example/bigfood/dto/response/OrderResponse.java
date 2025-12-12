package com.example.bigfood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    String id;
    String status;
    String createdAt;
    double totalAmount;
    int numberDishes;
    String rejectReason;
    InfoUserOrderResponse user;
}
