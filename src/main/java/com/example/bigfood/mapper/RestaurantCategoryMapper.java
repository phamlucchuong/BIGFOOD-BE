package com.example.bigfood.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.bigfood.dto.request.RestaurantCategoryCreateRequest;
import com.example.bigfood.dto.response.RestaurantCategoryResponse;
import com.example.bigfood.entity.RestaurantCategory;

@Mapper(componentModel = "spring")
public interface RestaurantCategoryMapper {
   @Mapping(target = "restaurants" , ignore = true)
   RestaurantCategory toRestaurantCategory(RestaurantCategoryCreateRequest request);
   RestaurantCategoryResponse toRestaurantCategoryResponse(RestaurantCategory restaurant);
} 
