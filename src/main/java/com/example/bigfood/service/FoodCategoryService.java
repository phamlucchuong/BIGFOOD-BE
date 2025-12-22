package com.example.bigfood.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.bigfood.dto.request.CreateFoodCategoryRequest;
import com.example.bigfood.dto.request.UpdateFoodCategoryRequest;
import com.example.bigfood.dto.response.FoodCategoryResponse;
import com.example.bigfood.entity.FoodCategory;
import com.example.bigfood.entity.Restaurant;
import com.example.bigfood.enums.ErrorCode;
import com.example.bigfood.exception.AppException;
import com.example.bigfood.mapper.FoodCategoryMapper;
import com.example.bigfood.repository.FoodCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class FoodCategoryService {
    FoodCategoryRepository foodCategoryRepository;
    FoodService foodService;
    RestaurantService restaurantService;
    FoodCategoryMapper foodCategoryMapper;

    
    public void hardDeleteFoodCategory(FoodCategory foodCategory) {
        if(foodCategory != null) foodCategoryRepository.delete(foodCategory);
    }


    public FoodCategoryResponse createFoodCategory(String userId, CreateFoodCategoryRequest request) {
        Restaurant restaurant = restaurantService.getRestaurantByUserId(userId);
        
        Set<FoodCategory> categories = restaurant.getFoodCategories();
        if(categories.stream()
            .anyMatch(cat -> cat.getName().equalsIgnoreCase(request.getName().trim()))) {
            throw new AppException(ErrorCode.FOOD_CATEGORY_ALREADY_EXISTS);
        }

        FoodCategory foodCategory = FoodCategory.builder()
            .name(request.getName())
            .iconIndex(request.getIconIndex())
            .restaurant(restaurant)
            .build();

        if(foodCategory != null) foodCategoryRepository.save(foodCategory);
        
        categories.add(foodCategory);
        restaurant.setFoodCategories(categories);
        restaurantService.saveRestaurant(restaurant);

        return foodCategoryMapper.toResponse(foodCategory);
    }

    public FoodCategoryResponse updateFoodCategory(String userId, UpdateFoodCategoryRequest request) {
        Restaurant restaurant = restaurantService.getRestaurantByUserId(userId);
        
        Set<FoodCategory> categories = restaurant.getFoodCategories();

        FoodCategory category = categories.stream()
            .filter(cat -> cat.getId().equals(request.getId()))
            .findFirst()
            .orElseThrow(() -> new AppException(ErrorCode.FOOD_CATEGORY_NOT_EXISTS));

        category.setName(request.getName());
        category.setIconIndex(request.getIconIndex());
        foodCategoryRepository.save(category);

        restaurant.setFoodCategories(categories);
        restaurantService.saveRestaurant(restaurant);

        return foodCategoryMapper.toResponse(category);
    }

    public Set<FoodCategoryResponse> getAllFoodCategories(String id) {
        Restaurant restaurant = restaurantService.getRestaurantByUserId(id);
        Set<FoodCategory> categories = restaurant.getFoodCategories();
        return foodCategoryMapper.toResponseSet(categories);
    }

    public void deleteFoodCategory(String userId, String categoryId) {
        Restaurant restaurant = restaurantService.getRestaurantByUserId(userId);
        
        Set<FoodCategory> categories = restaurant.getFoodCategories();

        FoodCategory category = categories.stream()
            .filter(cat -> cat.getId().equals(categoryId))
            .findFirst()
            .orElseThrow(() -> new AppException(ErrorCode.FOOD_CATEGORY_NOT_EXISTS));
        
        // nếu có món ăn còn trong danh mục thì không được xóa
        if(foodService.hasAvailableFood(categoryId)) {
            throw new AppException(ErrorCode.FOOD_CATEGORY_HAS_FOODS);
        }

        // nếu có món ăn đã xóa mềm thì chỉ đánh dấu xóa mềm danh mục
        if(foodService.hasFoods(categoryId)) {
            category.setDeleted(true);
            return;
        }
        
        categories.remove(category);
        restaurant.setFoodCategories(categories);
        restaurantService.saveRestaurant(restaurant);
    }
}
