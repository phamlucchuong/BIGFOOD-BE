package com.example.bigfood.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bigfood.dto.request.CreateOrderRequest;
import com.example.bigfood.dto.response.GoongResponse.GoongLocation;
import com.example.bigfood.dto.response.OrderResponse;
import com.example.bigfood.entity.Order;
import com.example.bigfood.entity.Restaurant;
import com.example.bigfood.entity.User;
import com.example.bigfood.mapper.OrderMapper;
import com.example.bigfood.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    RestaurantService restaurantService;
    UserService userService;
    OrderRepository orderRepository;
    GoongService goongService;
    OrderDetailService orderDetailService;
    OrderMapper orderMapper;
    
    
    @Transactional
    public OrderResponse createOrder(String userId, CreateOrderRequest request) {
        Restaurant restaurant = restaurantService.getRestaurantById(request.getRestaurantId());
        User user = userService.getUserById(userId);

        GoongLocation location = goongService.getGeocoding(request.getDeliveryAddress());

        double deliveryDistance = goongService.getDrivingDistance(
            restaurant.getLatitude(), restaurant.getLongitude(),
            location.getLat(), location.getLng()
        );

        Order order = Order.builder()
            .user(user)
            .restaurant(restaurant)
            .deliveryAddress(request.getDeliveryAddress())
            .deliveryLatitude(location.getLat())
            .deliveryLongitude(location.getLng())
            .deliveryFee(deliveryDistance * 0.1)
            .totalAmount(deliveryDistance * 0.1)
            .paymentMethod(request.getPaymentMethod())
            .notes(request.getNotes())
            .build();
    
        double totalPrice = orderDetailService.createSetOrderDetails(order, request.getOrderDetails());
        order.setTotalAmount(totalPrice + order.getDeliveryFee());

        orderRepository.save(order);
        return orderMapper.toResponse(order);
    }


    public List<OrderResponse> getAllOrdersByUserId(String userId) {
        return orderMapper.toResponseList(
            orderRepository.findByUser_Id(userId)
        );
    }


    public List<OrderResponse> getAllOrders() {
        return orderMapper.toResponseList(
            orderRepository.findAll()
        );
    }
    
}
