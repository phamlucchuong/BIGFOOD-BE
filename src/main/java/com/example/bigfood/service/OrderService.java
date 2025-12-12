package com.example.bigfood.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bigfood.dto.request.CreateOrderRequest;
import com.example.bigfood.dto.request.UpdateOrderStatusRequest;
import com.example.bigfood.dto.response.GoongResponse.GoongLocation;
import com.example.bigfood.dto.response.InfoUserOrderResponse;
import com.example.bigfood.dto.response.OrderDetailResponse;
import com.example.bigfood.dto.response.OrderFoodDetailResponse;
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
    CloudinaryService cloudinaryService;

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
        orderRepository.save(order);
        return orderMapper.toResponse(order);
    }

    public List<OrderResponse> getAllOrdersByRestaurantId(String restaurantId) {
        List<Order> order = orderRepository.findByRestaurant_UserId(restaurantId);
        return orderMapper.toResponseList(order);
    }

    public OrderDetailResponse getOrderDetailByOrderId(String orderId) {
         Order orderData = getOrderById(orderId);

         Set<OrderFoodDetailResponse> orderFoodDetails = orderData.getOrderDetails() != null
                 ? orderData.getOrderDetails().stream()
                         .map(detail -> OrderFoodDetailResponse.builder()
                                 .id(detail.getId())
                                 .foodName(detail.getFoodName())
                                 .quantity(String.valueOf(detail.getQuantity())) 
                                 .unitPrice(String.valueOf(detail.getUnitPrice())) 
                                 .totalPrice(String.valueOf(detail.getTotalPrice())) 
                                 .imageId(cloudinaryService.generateUrl(detail.getFood().getImageId()))
                                 .notes(detail.getNotes()) 
                                 .build())
                         .collect(Collectors.toSet())
                 : new HashSet<>();

          return OrderDetailResponse.builder()
                    .id(orderData.getId())
                    .status(orderData.getStatus().name())
                    .createdAt(orderData.getCreatedAt().toString())
                    .updatedAt(orderData.getUpdatedAt().toString())
                    .deliveryAddress(orderData.getDeliveryAddress())
                    .deliveryDistance(orderData.getDeliveryDistance())
                    .deliveryFee(orderData.getDeliveryFee())
                    .totalAmount(orderData.getTotalAmount())
                    .paymentMethod(orderData.getPaymentMethod())
                    .notes(orderData.getNotes())
                    .cancellReason(orderData.getCancellReason())
                    .rejectReason(orderData.getRejectReason())
                    .numberDishes(orderData.getOrderDetails() != null ? orderData.getOrderDetails().size() : 0)
                    .orderDetails(orderFoodDetails)
                    .user(InfoUserOrderResponse.builder()
                            .id(orderData.getUser().getId())
                            .name(orderData.getUser().getName())
                            .phone(orderData.getUser().getPhone())
                            .build())
                    .build();
    }

    public List<OrderResponse> getLoadStatusFilter(String restaurantId, String filter) {
        OrderStatus statusEnum;
        try {
            statusEnum = OrderStatus.valueOf(filter); 
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid status: " + filter);
        }

        List<Order> listOrder = orderRepository.findByStatus(restaurantId, statusEnum);
        return orderMapper.toResponseList(listOrder);
    }

    public Order getOrderById(String orderId) {
        if(orderId == null || orderId.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

}
