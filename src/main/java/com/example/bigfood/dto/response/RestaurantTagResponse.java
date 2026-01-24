package com.example.bigfood.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RestaurantTagResponse {
    String id;
    String restaurantName;
    String address;
    String userName;
    String email;
    String lisence;
    LocalDateTime createdAt;
}
