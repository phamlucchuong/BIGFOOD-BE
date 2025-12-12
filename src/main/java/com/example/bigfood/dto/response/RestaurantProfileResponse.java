package com.example.bigfood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantProfileResponse {
    String id;
    String restaurantName;
    String address;
    String phone;
    String email;
    String nameBank;
    String bankNumber;
    String bankAccountName;
    String avatar ;
    String bannerId;
    Boolean isApproved;
}
