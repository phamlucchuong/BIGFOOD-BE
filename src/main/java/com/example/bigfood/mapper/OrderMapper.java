package com.example.bigfood.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.bigfood.dto.response.OrderResponse;
import com.example.bigfood.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toResponse(Order order);

    List<OrderResponse> toResponseList(List<Order> all);
}
