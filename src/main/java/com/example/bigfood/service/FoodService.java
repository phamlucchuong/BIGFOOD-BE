package com.example.bigfood.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bigfood.dto.request.CreateFoodRequest;
import com.example.bigfood.dto.response.FoodResponse;
import com.example.bigfood.entity.Food;
import com.example.bigfood.entity.FoodCategory;
import com.example.bigfood.entity.Restaurant;
import com.example.bigfood.enums.ErrorCode;
import com.example.bigfood.exception.AppException;
import com.example.bigfood.mapper.FoodMapper;
import com.example.bigfood.repository.FoodRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class FoodService {
    FoodRepository foodRepository;
    CloudinaryService cloudinaryService;
    FoodMapper foodMapper;
    RestaurantService restaurantService;

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

    public List<FoodResponse> getAllByUserId(String userId) {
        return foodMapper.toListFoodResponses(foodRepository.findAllByRestaurantUserId(userId));
    }

    public void deleteFoodById(String itemId) {
        foodRepository.deleteById(itemId);
    }


    public FoodResponse createFood(String userId, String categoryId, CreateFoodRequest request) throws IOException {
        Restaurant restaurant = restaurantService.getRestaurantByUserId(userId);
        FoodCategory category = restaurant.getFoodCategories().stream()
            .filter(cat -> cat.getId().equals(categoryId))
            .findFirst()
            .orElseThrow(() -> new AppException(ErrorCode.FOOD_CATEGORY_NOT_EXISTS));

        Food food = createNewFood(category, request);
       
        category.getFoods().add(food);
        restaurantService.saveRestaurant(restaurant);
        return foodMapper.toFoodResponse(food);
    }

    public List<FoodResponse> getAllFood(String userId) {
        return getAllByUserId(userId);
    }

    public void deleteFood(String userId, String foodId) throws IOException {
        Restaurant restaurant = restaurantService.getRestaurantByUserId(userId);
        List<Food> foods = restaurant.getFoodCategories().stream()
            .flatMap(cat -> cat.getFoods().stream())
            .toList();

        Food food = foods.stream()
            .filter(f -> f.getId().equals(foodId))
            .findFirst()
            .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_EXISTS));


        // xóa file ảnh món ăn trên Cloudinary
        cloudinaryService.deleteFile(food.getImageId());

        // nếu đã có đơn hàng liên quan đến món ăn thì chỉ đánh dấu xóa mềm
        if(food.getCount() > 0) {
            food.setDeleted(true);
            return;
        }

        // xóa món ăn khỏi danh mục và nhà hàng
        FoodCategory category = food.getCategory();
        category.getFoods().remove(food);
        restaurantService.saveRestaurant(restaurant);
    }
}
