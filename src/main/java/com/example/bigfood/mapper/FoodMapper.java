package com.example.bigfood.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.bigfood.dto.request.CreateFoodRequest;
import com.example.bigfood.dto.response.FoodResponse;
import com.example.bigfood.entity.Food;

@Mapper(componentModel = "spring")
public interface FoodMapper {
    @Mapping(target = "categoryName", source = "category.name")
    FoodResponse toFoodResponse(Food food);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageId", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "available", ignore = true)
    @Mapping(target = "count", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    Food toFood(CreateFoodRequest request);

    List<FoodResponse> toListFoodResponses(List<Food> foods);
}
