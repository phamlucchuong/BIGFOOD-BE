package com.example.bigfood.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.bigfood.dto.request.CreateFoodCategoryRequest;
import com.example.bigfood.dto.request.CreateFoodRequest;
import com.example.bigfood.dto.request.CreateRestaurantRequest;
import com.example.bigfood.dto.request.UpdateFoodCategoryRequest;
import com.example.bigfood.dto.response.FoodCategoryResponse;
import com.example.bigfood.dto.response.FoodResponse;
import com.example.bigfood.dto.response.RestaurantResponse;
import com.example.bigfood.entity.Food;
import com.example.bigfood.entity.FoodCategory;
import com.example.bigfood.entity.Restaurant;
import com.example.bigfood.entity.User;
import com.example.bigfood.enums.ErrorCode;
import com.example.bigfood.exception.AppException;
import com.example.bigfood.mapper.FoodCategoryMapper;
import com.example.bigfood.mapper.FoodMapper;
import com.example.bigfood.mapper.RestaurantMapper;
import com.example.bigfood.repository.RestaurantRepository;
import com.example.bigfood.service.GoongService.GoongLocation;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantService {
    RestaurantRepository restaurantRepository;
    UserService userService;
    RestaurantMapper restaurantMapper;
    GoongService goongService;
    CloudinaryService cloudinaryService;
    FoodCategoryMapper foodCategoryMapper;
    FoodMapper foodMapper;
    FoodCategoryService foodCategoryService;
    FoodService foodService;

    public boolean existsByUserId(String userId) {
        return restaurantRepository.existsByUserId(userId);
    }

    public Restaurant getRestaurantByUserId(String userId) {
        return restaurantRepository.findByUserId(userId)
            .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTS));
    }

    public RestaurantResponse createNewRestaurant(String userId, CreateRestaurantRequest request) 
        throws AppException, IOException
        {
        User user = userService.getUserById(userId);

        if(existsByUserId(userId)) {
            throw new AppException(ErrorCode.RESTAURANT_ALREADY_EXISTS);
        }
        
        Restaurant restaurant = restaurantMapper.toRestaurant(request);
        
        // chuyển đổi địa chỉ thành tọa độ từ Goong API
        GoongLocation location = goongService.getGeocoding(request.getAddress());
        restaurant.setLatitude(location.getLat());
        restaurant.setLongitude(location.getLng());
        restaurant.setUser(user);

        // tải file đăng ký kinh doanh lên Cloudinary
        String licenseId = cloudinaryService.uploadFile(request.getLicenseFile(), "licenses");
        restaurant.setLicenseId(licenseId);

        restaurant = restaurantRepository.save(restaurant);

        return restaurantMapper.toRestaurantResponse(restaurant);
    }

    public RestaurantResponse getRestaurant(String userId) {
        return restaurantRepository.findById(userId)
            .map(restaurantMapper::toRestaurantResponse)
            .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTS));
    }

    public FoodCategoryResponse createFoodCategory(String userId, CreateFoodCategoryRequest request) {
        Restaurant restaurant = getRestaurantByUserId(userId);
        
        Set<FoodCategory> categories = restaurant.getFoodCategories();
        if(categories.stream()
            .anyMatch(cat -> cat.getName().equalsIgnoreCase(request.getName().trim()))) {
            throw new AppException(ErrorCode.FOOD_CATEGORY_ALREADY_EXISTS);
        }

        FoodCategory category = foodCategoryService.createNewFoodCategory(restaurant, request);

        categories.add(category);
        restaurant.setFoodCategories(categories);
        restaurantRepository.save(restaurant);

        return foodCategoryMapper.toResponse(category);
    }

    public FoodCategoryResponse updateFoodCategory(String userId, UpdateFoodCategoryRequest request) {
        Restaurant restaurant = getRestaurantByUserId(userId);
        
        Set<FoodCategory> categories = restaurant.getFoodCategories();

        FoodCategory category = categories.stream()
            .filter(cat -> cat.getId().equals(request.getId()))
            .findFirst()
            .orElseThrow(() -> new AppException(ErrorCode.FOOD_CATEGORY_NOT_EXISTS));

        category.setName(request.getName());
        category.setIconIndex(request.getIconIndex());

        restaurant.setFoodCategories(categories);
        restaurantRepository.save(restaurant);

        return foodCategoryMapper.toResponse(category);
    }

    public Set<FoodCategoryResponse> getAllFoodCategories(String id) {
        Restaurant restaurant = getRestaurantByUserId(id);
        Set<FoodCategory> categories = restaurant.getFoodCategories();
        return foodCategoryMapper.toResponseSet(categories);
    }

    public void deleteFoodCategory(String userId, String categoryId) {
        Restaurant restaurant = getRestaurantByUserId(userId);
        
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
        restaurantRepository.save(restaurant);
    }


    public FoodResponse createFood(String userId, String categoryId, CreateFoodRequest request) throws IOException {
        Restaurant restaurant = getRestaurantByUserId(userId);
        FoodCategory category = restaurant.getFoodCategories().stream()
            .filter(cat -> cat.getId().equals(categoryId))
            .findFirst()
            .orElseThrow(() -> new AppException(ErrorCode.FOOD_CATEGORY_NOT_EXISTS));

        Food food = foodService.createNewFood(category, request);
       
        category.getFoods().add(food);
        restaurantRepository.save(restaurant);
        return foodMapper.toFoodResponse(food);
    }

    public List<FoodResponse> getAllFood(String userId) {
        return foodService.getAllByUserId(userId);
    }

    public void deleteFood(String userId, String foodId) throws IOException {
        Restaurant restaurant = getRestaurantByUserId(userId);
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
        restaurantRepository.save(restaurant);
    }

    
}
