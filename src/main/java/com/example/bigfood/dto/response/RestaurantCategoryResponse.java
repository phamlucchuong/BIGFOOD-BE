package com.example.bigfood.dto.response;

import java.util.Set;

import com.example.bigfood.entity.Restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantCategoryResponse {
    String id;
    String name;
    Set<Restaurant> restaurants;
}
