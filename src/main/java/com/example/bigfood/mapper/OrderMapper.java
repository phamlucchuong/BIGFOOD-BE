package com.example.bigfood.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.bigfood.dto.response.OrderResponse;
import com.example.bigfood.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "status", expression = "java(order.getStatus().name())")
    @Mapping(target = "createdAt", expression = "java(order.getCreatedAt().toString())")
    @Mapping(target = "numberDishes",
            expression = "java(order.getOrderDetails() != null ? order.getOrderDetails().size() : 0)")
    @Mapping(target = "user", source = "user") 
    OrderResponse toResponse(Order order);

    List<OrderResponse> toResponseList(List<Order> all);
}
