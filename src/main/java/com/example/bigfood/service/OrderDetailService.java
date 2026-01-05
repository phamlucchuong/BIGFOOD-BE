package com.example.bigfood.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bigfood.dto.request.OrderDetailRequest;
import com.example.bigfood.entity.Food;
import com.example.bigfood.entity.Order;
import com.example.bigfood.entity.OrderDetail;
import com.example.bigfood.enums.ErrorCode;
import com.example.bigfood.exception.AppException;
import com.example.bigfood.repository.OrderDetailRepository;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private FoodService foodService;

    public OrderDetail getOrderDetailById(String id) {
        if(id == null || id.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_DETAIL_ID_EMPTY);
        }
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_FOUND));
    }

    public double createSetOrderDetails(Order order, Set<OrderDetailRequest> request) {
        Set<OrderDetail> orderDetails = new HashSet<>();
        double totalPrice = 0;
        
        for (OrderDetailRequest detailRequest : request) {
            Food food = foodService.getFoodById(detailRequest.getFoodId());
            
            // Build OrderDetail with bidirectional relationship
            OrderDetail orderDetail = OrderDetail.builder()
                    .foodName(food.getName())
                    .quantity(detailRequest.getQuantity())
                    // .unitPrice(food.getPrice())
                    // .totalPrice(food.getPrice() * detailRequest.getQuantity())
                    .notes(detailRequest.getNotes())
                    .order(order)  // Set parent reference (important for cascade)
                    .food(food)
                    .build();
            
            orderDetails.add(orderDetail);
            
            // Update food statistics
            foodService.increaseCount(food.getId(), detailRequest.getQuantity());
            // totalPrice += food.getPrice() * detailRequest.getQuantity();
        }

        order.setOrderDetails(orderDetails);
        return totalPrice;
    }
}
