package com.example.bigfood.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.bigfood.dto.request.UpdateFoodCategoryRequest;
import com.example.bigfood.dto.response.FoodCategoryResponse;
import com.example.bigfood.entity.FoodCategory;

@Mapper(componentModel = "spring", uses = {FoodMapper.class})
public interface FoodCategoryMapper {
    @Mapping(target = "numberFood",
             expression = "java(category.getFoods() != null ? category.getFoods().size() : 0)")
    FoodCategoryResponse toResponse(FoodCategory category);

    Set<FoodCategoryResponse> toResponseSet(Set<FoodCategory> categories);

    FoodCategory toFoodCategory(UpdateFoodCategoryRequest request);
}
