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
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_FOUND));
    }

    public double createSetOrderDetails(Order order, Set<OrderDetailRequest> request) {
        Set<OrderDetail> orderDetails = new HashSet<>();
        double totalPrice = 0;
        
        for (OrderDetailRequest detailRequest : request) {
            Food food = foodService.getFoodById(detailRequest.getFoodId());
            orderDetails.add(OrderDetail.builder()
                    .foodName(food.getName())
                    .quantity(detailRequest.getQuantity())
                    .unitPrice(food.getPrice())
                    .totalPrice(food.getPrice() * detailRequest.getQuantity())
                    .notes(detailRequest.getNotes())
                    .order(order)
                    .food(food)
                    .build());
            
            foodService.increaseCount(detailRequest.getQuantity(), food.getId());
            totalPrice += food.getPrice() * detailRequest.getQuantity();
        }

        order.setOrderDetails(orderDetails);
        return totalPrice;

    }
    // public Set<OrderDetail> createSetOrderDetails(Order order, Set<OrderDetailRequest> request) {
    //     Set<OrderDetail> orderDetails = new HashSet<>();

    //     for (OrderDetailRequest detailRequest : request) {
    //         Food food = foodService.getFoodById(detailRequest.getFoodId());
    //         orderDetails.add(OrderDetail.builder()
    //                 .foodName(food.getName())
    //                 .quantity(detailRequest.getQuantity())
    //                 .unitPrice(food.getPrice())
    //                 .totalPrice(food.getPrice() * detailRequest.getQuantity())
    //                 .notes(detailRequest.getNotes())
    //                 .order(order)
    //                 .food(food)
    //                 .build());

    //     }

    //     orderDetailRepository.saveAll(orderDetails);
    //     return orderDetails;

    // }
}
