package com.example.bigfood.dto.request;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

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
public class UpdateRestaurantRequest {
    String restaurantName;
    String address;
    String phone;
    String email;
    String nameBank;
    String bankNumber;
    String bankAccountName;
    double latitude;
    double longitude;
    MultipartFile banner;
    MultipartFile avatar;
}
