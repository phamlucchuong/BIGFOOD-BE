package com.example.bigfood.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.example.bigfood.dto.response.OrderDetailResponse;
import com.example.bigfood.dto.response.OrderItemResponse;
import com.example.bigfood.entity.OrderDetail;

@Mapper(componentModel = "spring", uses = { com.example.bigfood.service.CloudinaryService.class })
public interface OrderDetailMapper {
    Set<OrderDetailResponse> toDetailResponseSet(Set<OrderDetail> orderDetail);

    @Mapping(target = "image", source = "food.imageId", qualifiedByName = "generateUrl")
    @Mapping(target = "foodName", source = "foodName")
    @Mapping(target = "unitPrice", source = "unitPrice")
    OrderItemResponse toOrderItemResponse(OrderDetail orderDetail);

    @Named("toOrderItemResponseSet")
    default Set<OrderItemResponse> toOrderItemResponseSet(Set<OrderDetail> orderDetails) {
        if (orderDetails == null) {
            return null;
        }
        return orderDetails.stream()
                .map(this::toOrderItemResponse)
                .collect(java.util.stream.Collectors.toSet());
    }
}
