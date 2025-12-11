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

    /**
     * Creates a new order with order details.
     * 
     * Production Best Practices implemented:
     * 1. @Transactional: Ensures atomicity - all operations succeed or rollback together
     * 2. Cascade persistence: OrderDetails auto-saved via CascadeType.ALL + orphanRemoval
     * 3. Bidirectional relationship: Parent-child properly linked before persistence
     * 4. Single save operation: Only save Order, OrderDetails cascade automatically
     * 
     * Transaction scope ensures:
     * - If any operation fails (geocoding, distance calc, validation), entire order is rolled back
     * - Database consistency is maintained
     * - No orphaned OrderDetails if Order creation fails
     * 
     * @param userId User creating the order
     * @param request Order creation request containing order details
     * @return OrderResponse with order and order details
     */
    @Transactional
    public OrderResponse createOrder(String userId, CreateOrderRequest request) {
        // Validate and fetch related entities
        Restaurant restaurant = restaurantService.getRestaurantByUserId(request.getRestaurantId());
        User user = userService.getUserById(userId);

        // Get delivery location coordinates
        GoongLocation location = goongService.getGeocoding(request.getDeliveryAddress());

        // Calculate delivery distance and fee
        double deliveryDistance = goongService.getDrivingDistance(
                restaurant.getLatitude(), restaurant.getLongitude(),
                location.getLat(), location.getLng());

        // Build Order entity (parent)
        Order order = Order.builder()
                .user(user)
                .restaurant(restaurant)
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryLatitude(location.getLat())
                .deliveryLongitude(location.getLng())
                .deliveryDistance(deliveryDistance)
                .deliveryFee(deliveryDistance * 3000)
                .paymentMethod(request.getPaymentMethod())
                .notes(request.getNotes())
                .build();

        // Create OrderDetails (children) and establish bidirectional relationship
        // OrderDetails are not yet persisted, just linked to Order
        double totalPrice = orderDetailService.createSetOrderDetails(order, request.getOrderDetails());
        order.setTotalAmount(totalPrice + order.getDeliveryFee());

        // Single save operation - Order and OrderDetails cascade automatically
        // CascadeType.ALL ensures OrderDetails are persisted with Order
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

    public Order getOrderById(String orderId) {
        if(orderId == null || orderId.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

}
