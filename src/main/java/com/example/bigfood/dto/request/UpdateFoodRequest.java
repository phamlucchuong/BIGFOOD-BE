package com.example.bigfood.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateFoodRequest {
    String foodId;
    String name;
    String categoryId;
    String description;
    MultipartFile image;
    double price;
    boolean available;
}
