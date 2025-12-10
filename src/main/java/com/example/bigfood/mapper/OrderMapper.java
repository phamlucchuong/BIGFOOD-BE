package com.example.bigfood.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.bigfood.dto.response.OrderResponse;
import com.example.bigfood.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "orderId", source = "id")
    OrderResponse toResponse(Order order);

    List<OrderResponse> toResponseList(List<Order> all);
}
