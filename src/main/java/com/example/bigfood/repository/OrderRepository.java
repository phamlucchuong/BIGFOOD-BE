package com.example.bigfood.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bigfood.entity.Order;
import com.example.bigfood.enums.OrderStatus;



@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUser_Id(String userId);

    List<Order> findByRestaurant_UserId(String restaurantId);
    @Query("""
            SELECT o FROM Order o
            Where(:status IS NULL OR o.status = :status)
            AND o.restaurant.userId = :restaurantId
            """)
    List<Order> findByStatus(
            @Param("restaurantId") String restaurantId,
            @Param("status") OrderStatus status);
}
