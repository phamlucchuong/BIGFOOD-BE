package com.example.bigfood.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.bigfood.dto.request.CreateFoodRequest;
import com.example.bigfood.dto.request.UpdateFoodRequest;
import com.example.bigfood.dto.response.FoodResponse;
import com.example.bigfood.dto.response.PageResponse;
import com.example.bigfood.entity.Food;
import com.example.bigfood.entity.FoodCategory;
import com.example.bigfood.entity.Restaurant;
import com.example.bigfood.enums.ErrorCode;
import com.example.bigfood.exception.AppException;
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
    RestaurantService restaurantService;
    FoodOptionService foodOptionService;

    public Food getFoodById(String itemId) {
        if(itemId == null) throw new AppException(ErrorCode.FOOD_NOT_EXISTS);
        return foodRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Food not found"));
    }

    public boolean hasAvailableFood(String categoryId) {
        return foodRepository.countByCategory_IdAndAvailableTrue(categoryId) > 0;
    }

    public boolean hasFoods(String categoryId) {
        return foodRepository.countByCategoryId(categoryId) > 0;
    }

    public void increaseCount(String foodId, int amount) {
        foodRepository.increaseCountById(foodId, amount);
    }

    public Food createNewFood(FoodCategory category, CreateFoodRequest request ) throws IOException {
        String imageId = cloudinaryService.uploadFile(request.getImage(), "foods");
        Food food = Food.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .imageId(imageId)
                        .category(category)
                        .build();
        if(food == null) throw new AppException(ErrorCode.FOOD_CREATION_FAILED);
        Food foodData = foodRepository.save(food);
        if(request.getFoodOptions() != null && !request.getFoodOptions().isEmpty()){
            request.getFoodOptions().forEach(x -> {
                foodOptionService.createFoodOption(x.getName(), x.getPrice(), x.getDefaultPrice() ,foodData);
            });
        }
         return foodData;
    }
    
    public Food update(UpdateFoodRequest request) throws IOException {
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
        if(request.getCategoryId() != null && !request.getCategoryId().isEmpty()) {
        FoodCategory category = foodCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Food category not found"));
            food.setCategory(category);
        }
        food.setAvailable(request.isAvailable());
        if(request.getFoodOptions() != null && !request.getFoodOptions().isEmpty()){
           food.getFoodOptions().clear();
            if(!request.getFoodOptions().isEmpty()) {
                request.getFoodOptions().forEach(x -> {
                    foodOptionService.createFoodOption(x.getName(), x.getPrice(), x.getDefaultPrice() ,food);
                });
            }
        }
    }

    public PageResponse<FoodResponse> getAllByUserId(String userId , Integer page) {
        int limit = 10;
        int pageCurrent = (page != null && page > 0) ? page - 1 : 0;

        Pageable pageable = PageRequest.of(pageCurrent, limit , Sort.by(Sort.Direction.DESC, "name"));
        var pageData = foodRepository.findAllByRestaurantUserId(userId ,  pageable);
        return PageResponse.<FoodResponse>builder()
                .items(pageData.getContent().stream()
                    .map(foodMapper::toFoodResponse).toList())
                .total(pageData.getTotalElements())
                .page(pageCurrent+1)
                .pageSize(limit)
                .totalPages(pageData.getTotalPages())
                .build();
    }

    public List<FoodResponse> listFoodByCategoryId(String userId, String categoryId) {
        if (categoryId == null
                || categoryId.isBlank()
                || categoryId.equalsIgnoreCase("all")) {
            categoryId = null;
        }
        List<Food> foods = foodRepository.findFoodByCategoryAndRestaurant(categoryId, userId);
        if (foods.isEmpty()) {
            throw new AppException(ErrorCode.FOOD_CATEGORY_NOT_EXISTS);
        }
        return foodMapper.toListFoodResponses(foods ,  cloudinaryService);
    }

    public void deleteFoodById(String itemId) {
        if(itemId == null) return;
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

     public FoodResponse updateFood(String userId,UpdateFoodRequest request) throws IOException {
        Restaurant restaurant = restaurantService.getRestaurantByUserId(userId);
        Food food = update(request);
        FoodCategory category = restaurant.getFoodCategories().stream()
            .filter(cat -> cat.getId().equals(food.getCategory().getId()))
            .findFirst()
            .orElseThrow(() -> new AppException(ErrorCode.FOOD_CATEGORY_NOT_EXISTS));
        category.getFoods().add(food);
        restaurantService.saveRestaurant(restaurant);
        return foodMapper.toFoodResponse(food);
    }

    public List<FoodResponse> getTop5BestSellingFoods(String restaurantId) {
        List<Food> foods = foodRepository.findBySoldDSECFoods(restaurantId);
         foods = foods.subList(0, Math.min(5, foods.size()));
         return foodMapper.toListFoodResponses(foods, cloudinaryService);
    }

    public List<FoodResponse> getTop5LeastSellingFoods(String restaurantId) {
        List<Food> foods = foodRepository.findBySoldASCFoods(restaurantId); 
          foods = foods.subList(0, Math.min(5, foods.size()));
         return foodMapper.toListFoodResponses(foods, cloudinaryService);
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
        if(food.getSold() > 0) {
            food.setDeleted(true);
            return;
        }

        // xóa món ăn khỏi danh mục và nhà hàng
        FoodCategory category = food.getCategory();
        category.getFoods().remove(food);
        restaurantService.saveRestaurant(restaurant);
    }
}
