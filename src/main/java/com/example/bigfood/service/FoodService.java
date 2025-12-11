package com.example.bigfood.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bigfood.dto.request.CreateFoodRequest;
import com.example.bigfood.dto.request.UpdateFoodRequest;
import com.example.bigfood.dto.response.FoodResponse;
import com.example.bigfood.entity.Food;
import com.example.bigfood.entity.FoodCategory;
import com.example.bigfood.mapper.FoodMapper;
import com.example.bigfood.repository.FoodCategoryRepository;
import com.example.bigfood.repository.FoodRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class FoodService {
    FoodCategoryRepository foodCategoryRepository;
    FoodRepository foodRepository;
    CloudinaryService cloudinaryService;
    FoodMapper foodMapper;

    public Food getFoodById(String itemId) {
        return foodRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Food not found"));
    }

    public boolean hasAvailableFood(String categoryId) {
        return foodRepository.countByCategory_IdAndIsAvailableTrue(categoryId) > 0;
    }

    public boolean hasFoods(String categoryId) {
        return foodRepository.countByCategoryId(categoryId) > 0;
    }

    public void increaseCount(int amount, String foodId) {
        foodRepository.increaseCountById(foodId, amount);
    }

    public Food createNewFood(FoodCategory category, CreateFoodRequest request) throws IOException {
        String imageId = cloudinaryService.uploadFile(request.getImage(), "foods");

        return foodRepository.save(
                Food.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .price(request.getPrice())
                        .imageId(imageId)
                        .category(category)
                        .build());
    }
    
    public Food updateFood(UpdateFoodRequest request) throws IOException {
        Food food = getFoodById(request.getFoodId());
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageId = cloudinaryService.uploadFile(request.getImage(), "foods");
            food.setImageId(imageId);
        }
        applyUpdateFoodRequestToFood(food, request);
        return foodRepository.save(food);
    }

    private void applyUpdateFoodRequestToFood(Food food, UpdateFoodRequest request) {
       if(request.getName() != null && !request.getName().isEmpty()) {
           food.setName(request.getName());
        }
         if(request.getDescription() != null && !request.getDescription().isEmpty()) {
              food.setDescription(request.getDescription());
        }
        if(request.getPrice() != 0 && request.getPrice() > 0) {
            food.setPrice(request.getPrice());
        }
        if(request.getCategoryId() != null && !request.getCategoryId().isEmpty()) {
        FoodCategory category = foodCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Food category not found"));
            food.setCategory(category);
        }
        food.setAvailable(request.isAvailable());
    }

    public List<FoodResponse> getAllByUserId(String userId) {
        return foodMapper.toListFoodResponses(foodRepository.findAllByRestaurantUserId(userId));
    }

    public void deleteFoodById(String itemId) {
        foodRepository.deleteById(itemId);
    }
}
