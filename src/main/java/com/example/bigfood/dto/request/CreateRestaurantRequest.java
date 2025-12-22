package com.example.bigfood.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateRestaurantRequest {
    List<String> categoryIds;
    String restaurantName;
    String address;
    String nameBank;
    String bankNumber;
    String bankAccountName;
    MultipartFile licenseFile;
}
