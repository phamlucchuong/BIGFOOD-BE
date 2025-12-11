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

    /**
     * Creates OrderDetails and associates them with the Order.
     * Uses JPA Cascade (CascadeType.ALL + orphanRemoval) from Order entity.
     * OrderDetails will be automatically persisted when Order is saved.
     * 
     * Best practice in production:
     * - Bidirectional relationship: Order -> OrderDetails and OrderDetail -> Order
     * - Cascade operations from parent (Order) to children (OrderDetails)
     * - No need to manually save OrderDetails repository
     * 
     * @param order The parent Order entity
     * @param request Set of OrderDetailRequest
     * @return Total price of all order details
     */
    public double createSetOrderDetails(Order order, Set<OrderDetailRequest> request) {
        Set<OrderDetail> orderDetails = new HashSet<>();
        double totalPrice = 0;
        
        for (OrderDetailRequest detailRequest : request) {
            Food food = foodService.getFoodById(detailRequest.getFoodId());
            
            // Build OrderDetail with bidirectional relationship
            OrderDetail orderDetail = OrderDetail.builder()
                    .foodName(food.getName())
                    .quantity(detailRequest.getQuantity())
                    .unitPrice(food.getPrice())
                    .totalPrice(food.getPrice() * detailRequest.getQuantity())
                    .notes(detailRequest.getNotes())
                    .order(order)  // Set parent reference (important for cascade)
                    .food(food)
                    .build();
            
            orderDetails.add(orderDetail);
            
            // Update food statistics
            foodService.increaseCount(food.getId(), detailRequest.getQuantity());
            totalPrice += food.getPrice() * detailRequest.getQuantity();
        }

        // Set children collection in parent (bidirectional relationship)
        order.setOrderDetails(orderDetails);
        
        // OrderDetails will be automatically saved when Order is saved
        // due to CascadeType.ALL in Order entity
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
