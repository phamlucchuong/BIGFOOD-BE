package com.example.bigfood.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bigfood.entity.Order;
import com.example.bigfood.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

        @Query("""
                        SELECT o FROM Order o
                        WHERE o.user.id = :userId
                          AND (:statusList IS NULL OR o.status IN :statusList)
                        ORDER BY o.createdAt DESC
                        """)
        Page<Order> findByUser_Id(
                        @Param("userId") String userId,
                        @Param("statusList") List<OrderStatus> statusList,
                        Pageable pageable);

        Page<Order> findByRestaurant_UserId(String restaurantId ,  Pageable pageable);

        @Query("""
                        SELECT o FROM Order o
                        Where(:status IS NULL OR o.status = :status)
                        AND o.restaurant.userId = :restaurantId
                        """)
        Page<Order> findByStatus(
                        @Param("restaurantId") String restaurantId,
                        @Param("status") OrderStatus status ,
                        Pageable pageable
                      );

        @Query("""
                         SELECT o FROM Order o
                         WHERE o.restaurant.userId = :restaurantId
                        ORDER BY o.createdAt DESC
                         """)
        List<Order> findOrderNewList(@Param("restaurantId") String restaurantId);

        List<Order> findByRestaurant_UserIdAndCreatedAtBetween(
                        String restaurantId,
                        LocalDateTime start,
                        LocalDateTime end);

        long countByCreatedAtBetween(LocalDateTime startTimeCurrent, LocalDateTime endTimeCurrent);

        @Query(value = """
                        SELECT MONTH(o.created_at) as month, COUNT(o.id) as totalOrders
                        FROM orders o
                        WHERE YEAR(o.created_at) = YEAR(CURRENT_DATE)
                          AND MONTH(o.created_at) <= MONTH(CURRENT_DATE)
                        GROUP BY MONTH(o.created_at)
                        ORDER BY month ASC
                        """, nativeQuery = true)
        List<Object[]> countOrdersByMonthInCurrentYear();
}
