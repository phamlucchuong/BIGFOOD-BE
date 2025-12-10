package com.example.bigfood.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bigfood.dto.request.CreateOrderRequest;
import com.example.bigfood.dto.request.UpdateOrderStatusRequest;
import com.example.bigfood.dto.response.GoongResponse.GoongLocation;
import com.example.bigfood.dto.response.OrderResponse;
import com.example.bigfood.entity.Order;
import com.example.bigfood.entity.Restaurant;
import com.example.bigfood.entity.User;
import com.example.bigfood.enums.ErrorCode;
import com.example.bigfood.enums.OrderStatus;
import com.example.bigfood.exception.AppException;
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
        Restaurant restaurant = restaurantService.getRestaurantByUserId(request.getRestaurantId());
        User user = userService.getUserById(userId);

        GoongLocation location = goongService.getGeocoding(request.getDeliveryAddress());

        double deliveryDistance = goongService.getDrivingDistance(
                restaurant.getLatitude(), restaurant.getLongitude(),
                location.getLat(), location.getLng());

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
                orderRepository.findByUser_Id(userId));
    }

    public List<OrderResponse> getAllOrders() {
        return orderMapper.toResponseList(
                orderRepository.findAll());
    }

    public OrderResponse updateOrderStatus(String orderId, UpdateOrderStatusRequest request) {
        if (orderId == null || orderId.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if(order.getStatus().toString().equals(request.getStatus().toString())) {
            throw new AppException(ErrorCode.STATUS_SAME_AS_BEFORE);
        }
        order.setStatus(request.getStatus());

        if (request.getStatus().equals(OrderStatus.CANCELED)) {
            order.setCancellReason(request.getReason());
        }
        if (request.getStatus().equals(OrderStatus.REJECTED)) {
            order.setRejectReason(request.getReason());
        }

        // order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
        return orderMapper.toResponse(order);
    }

    public List<OrderResponse> getAllOrdersByRestaurantId(String restaurantId) {
        return orderMapper.toResponseList(
                orderRepository.findByRestaurant_UserId(restaurantId));
    }

}
