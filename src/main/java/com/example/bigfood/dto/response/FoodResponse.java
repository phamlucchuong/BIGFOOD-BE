package com.example.bigfood.dto.response;

import java.util.Set;

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
    String image;
    int sold;
    boolean available;
    boolean deleted;
    String categoryName;
    Set<FoodOptionResponse> foodOptions;
}
