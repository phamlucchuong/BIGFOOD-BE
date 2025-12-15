package com.example.bigfood.mapper;

import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.bigfood.dto.response.OrderFullResponse;
import com.example.bigfood.dto.response.OrderResponse;
import com.example.bigfood.dto.response.OrderShortResponse;
import com.example.bigfood.entity.Order;

@Mapper(componentModel = "spring", uses = {OrderDetailMapper.class, com.example.bigfood.service.CloudinaryService.class})
public interface OrderMapper {
    @Mapping(target = "status", expression = "java(order.getStatus().name())")
    @Mapping(target = "createdAt", expression = "java(order.getCreatedAt().toString())")
    @Mapping(target = "numberDishes",
            expression = "java(order.getOrderDetails() != null ? order.getOrderDetails().size() : 0)")
    @Mapping(target = "user", source = "user") 
    OrderResponse toResponse(Order order);

    @Mapping(target = "orderDetails", source = "orderDetails", qualifiedByName = "toOrderItemResponseSet")
    @Mapping(target = "restaurantName", source = "restaurant.restaurantName")
    OrderFullResponse toFullResponse(Order order);

    @Mapping(target = "restaurantName", source = "restaurant.restaurantName")
    @Mapping(target = "numberDishes", expression = "java(order.getOrderDetails() != null ? order.getOrderDetails().size() : 0)")
    @Mapping(target = "restaurantBanner", source = "restaurant.bannerId", qualifiedByName = "generateUrl")
    OrderShortResponse toShortResponse(Order order);

    List<OrderResponse> toResponseList(List<Order> all);
}
