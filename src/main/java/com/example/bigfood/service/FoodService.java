package com.example.bigfood.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bigfood.dto.request.CreateFoodRequest;
import com.example.bigfood.dto.response.FoodResponse;
import com.example.bigfood.entity.Food;
import com.example.bigfood.entity.FoodCategory;
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


    public boolean hasAvailableFood(String categoryId) {
        return foodRepository.countByCategory_IdAndIsAvailableTrue(categoryId) > 0;
    }

    public boolean hasFoods(String categoryId) {
        return foodRepository.countByCategoryId(categoryId) > 0;
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
                .build()
        );
    }

	public List<FoodResponse> getAllByUserId(String userId) {
		return foodMapper.toListFoodResponses(foodRepository.findAllByRestaurantUserId(userId));
	}
}
