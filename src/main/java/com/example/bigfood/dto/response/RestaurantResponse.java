package com.example.bigfood.dto.response;

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
public class RestaurantResponse {
    String userId;
    String restaurantName;
    String address;
    String nameBank;
    String bankNumber;
    String bankAccountName;
    double latitude;
    double longitude;
    String bannerId;
    String licenseId;
}
