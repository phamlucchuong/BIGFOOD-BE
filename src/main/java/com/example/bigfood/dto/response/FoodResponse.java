package com.example.bigfood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class FoodResponse {
    String id;
    String name;
    String description;
    String imageId;
    double price;
    int count;
    boolean isAvailable;
    boolean isDeleted;
    String categoryName;
}
