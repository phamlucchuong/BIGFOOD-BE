package com.example.bigfood.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.bigfood.dto.RestaurantProjection;
import com.example.bigfood.dto.request.CreateRestaurantRequest;
import com.example.bigfood.dto.response.RestaurantResponse;
import com.example.bigfood.dto.response.RestaurantTagResponse;
import com.example.bigfood.dto.response.RestaurantDetailResponse;
import com.example.bigfood.dto.response.RestaurantFullResponse;
import com.example.bigfood.entity.Restaurant;
import com.example.bigfood.service.CloudinaryService;


@Mapper(componentModel = "spring", uses = {FoodCategoryMapper.class, RestaurantCategoryMapper.class, CloudinaryService.class})
public interface RestaurantMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "approved", constant = "false")
    @Mapping(target = "restaurantCategories", ignore = true)
    @Mapping(target = "foodCategories", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "longitude", ignore = true)
    @Mapping(target = "latitude", ignore = true)
    @Mapping(target = "bannerId", ignore = true)
    @Mapping(target = "licenseId", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "location", ignore = true)
    Restaurant toRestaurant(CreateRestaurantRequest request);

    @Mapping(target = "id", source = "userId")
    RestaurantFullResponse toRestaurantResponse(Restaurant restaurant);

    @Mapping(target = "id", source = "userId")
    RestaurantDetailResponse toRestaurantDetailResponse(Restaurant restaurant);

    // @Mapping(target = "id", source = "userId")
    RestaurantFullResponse toRestaurantResponse(RestaurantResponse nearByRestaurantResponse);
    
    @Mapping(target = "id", source = "restaurantId")
    RestaurantResponse toRestaurantResponse(RestaurantProjection projection);

    @Mapping(target = "id", source = "userId")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "createdAt", source = "user.createdAt")
    @Mapping(target = "lisence", source = "licenseId", qualifiedByName = "generateUrl")
    RestaurantTagResponse toRestaurantTagResponse(Restaurant restaurant);
}