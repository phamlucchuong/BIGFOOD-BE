package com.example.bigfood.dto.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
public class RestaurantDetailResponse {
    String userId;
    String restaurantName;
    String address;
    String banner;
    Set<FoodCategoryResponse> foodCategories;
}
