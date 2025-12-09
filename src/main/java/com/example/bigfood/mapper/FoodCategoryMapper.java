package com.example.bigfood.mapper;

import java.util.Set;

import org.mapstruct.Mapper;

import com.example.bigfood.dto.request.UpdateFoodCategoryRequest;
import com.example.bigfood.dto.response.FoodCategoryResponse;
import com.example.bigfood.entity.FoodCategory;

@Mapper(componentModel = "spring", uses = {FoodMapper.class})
public interface FoodCategoryMapper {
    FoodCategoryResponse toResponse(FoodCategory foodCategory);

    Set<FoodCategoryResponse> toResponseSet(Set<FoodCategory> categories);

    FoodCategory toFoodCategory(UpdateFoodCategoryRequest request);
}
