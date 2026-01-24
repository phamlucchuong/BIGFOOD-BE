package com.example.bigfood.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    String id;
    String foodName;
    int quantity;
    String image;
    double unitPrice;
    double totalPrice;
    String notes;
}
