package com.example.bigfood.dto.response;

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
public class OrderFoodDetailResponse {
    String id;
    String foodName;
    String quantity;
    String unitPrice;
    String totalPrice;
    String imageId;
    String notes;
}
