package com.example.bigfood.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateFoodRequest {
    String name;
    String description;
    MultipartFile image;
    List<FoodSize> foodOptions;
}
