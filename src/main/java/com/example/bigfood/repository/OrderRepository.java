package com.example.bigfood.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bigfood.dto.FinanceProjection;
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

  Page<Order> findByRestaurant_UserId(String restaurantId, Pageable pageable);

  @Query("""
      SELECT o FROM Order o
      Where(:status IS NULL OR o.status = :status)
      AND o.restaurant.userId = :restaurantId
      """)
  Page<Order> findByStatus(
      @Param("restaurantId") String restaurantId,
      @Param("status") OrderStatus status,
      Pageable pageable);

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




  @Query(value = """
      SELECT 
          COALESCE(SUM(CASE 
              WHEN MONTH(o.created_at) = MONTH(CURRENT_DATE) 
              AND YEAR(o.created_at) = YEAR(CURRENT_DATE)
              AND o.status = 'COMPLETED'
              THEN o.total_amount * 0.2
              ELSE 0 
          END), 0) as monthIncome,
          
          CAST(CASE 
              WHEN COALESCE(SUM(CASE 
                  WHEN MONTH(o.created_at) = MONTH(CURRENT_DATE) 
                  AND YEAR(o.created_at) = YEAR(CURRENT_DATE)
                  AND o.status = 'COMPLETED'
                  THEN o.total_amount * 0.2
                  ELSE 0 
              END), 0) >= COALESCE(SUM(CASE 
                  WHEN MONTH(o.created_at) = MONTH(DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH))
                  AND YEAR(o.created_at) = YEAR(DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH))
                  AND o.status = 'COMPLETED'
                  THEN o.total_amount * 0.2
                  ELSE 0 
              END), 0)
              THEN 1
              ELSE 0
          END AS SIGNED) as monthDirection,
          
          COALESCE(SUM(CASE 
              WHEN DATE(o.created_at) = CURRENT_DATE
              AND o.status = 'COMPLETED'
              THEN o.total_amount * 0.2
              ELSE 0 
          END), 0) as dayIncome,
          
          CAST(CASE 
              WHEN COALESCE(SUM(CASE 
                  WHEN DATE(o.created_at) = CURRENT_DATE
                  AND o.status = 'COMPLETED'
                  THEN o.total_amount * 0.2
                  ELSE 0 
              END), 0) >= COALESCE(SUM(CASE 
                  WHEN DATE(o.created_at) = DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY)
                  AND o.status = 'COMPLETED'
                  THEN o.total_amount * 0.2
                  ELSE 0 
              END), 0)
              THEN 1
              ELSE 0
          END AS SIGNED) as dayDirection
      FROM orders o
      """, nativeQuery = true)
  FinanceProjection getFinanceSummary();

  @Query("""
      SELECT o FROM Order o
      WHERE DATE(o.createdAt) = CURRENT_DATE
      ORDER BY o.totalAmount DESC
      LIMIT 5
      """)
  List<Order> findTop5ByCreatedAtTodayOrderByTotalAmountDesc();


}
