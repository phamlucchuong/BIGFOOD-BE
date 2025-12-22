package com.example.bigfood.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RestaurantActiveResponse {
    String id;
    String name;
    String address;
    LocalDateTime approvedAt;
    List<String> categories;
    long totalOrders;
    double rating;
}
