package com.example.bigfood.mapper;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.bigfood.dto.request.CreateFoodRequest;
import com.example.bigfood.dto.response.FoodResponse;
import com.example.bigfood.entity.Food;
import com.example.bigfood.service.CloudinaryService;

@Mapper(componentModel = "spring", uses = { CloudinaryService.class })
public interface FoodMapper {
    @Mapping(target = "categoryName", ignore = true)
    @Mapping(target = "image", source = "imageId", qualifiedByName = "generateUrl")
    FoodResponse toFoodResponse(Food food);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageId", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "available", ignore = true)
    @Mapping(target = "sold", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    Food toFood(CreateFoodRequest request);
    
    @Mapping(target = "categoryName", expression = "java(food.getCategory() != null ? food.getCategory().getName() : null)")
    List<FoodResponse> toListFoodResponses(List<Food> foods , @Context CloudinaryService cloudinaryService);
}
