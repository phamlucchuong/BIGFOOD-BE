package com.example.bigfood.service;

import org.springframework.stereotype.Service;

import com.example.bigfood.dto.request.CreateFoodCategoryRequest;
import com.example.bigfood.entity.FoodCategory;
import com.example.bigfood.entity.Restaurant;
import com.example.bigfood.repository.FoodCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class FoodCategoryService {
    FoodCategoryRepository foodCategoryRepository;


    

    public FoodCategory createNewFoodCategory(Restaurant restaurant, CreateFoodCategoryRequest request) {
        FoodCategory foodCategory = FoodCategory.builder()
            .name(request.getName())
            .iconIndex(request.getIconIndex())
            .restaurant(restaurant)
            .build();

        return foodCategoryRepository.save(foodCategory);
    }


    public void deleteFoodCategory(FoodCategory foodCategory) {
        foodCategoryRepository.delete(foodCategory);
    }
}
