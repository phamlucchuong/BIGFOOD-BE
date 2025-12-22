package com.example.bigfood.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RestaurantResponse {
    String id;
    String restaurantName;
    String address;
    @Builder.Default
    Double distanceM = 0.0; // alias trong SELECT
    String banner;
    double rating;
    int reviewCount;
}
