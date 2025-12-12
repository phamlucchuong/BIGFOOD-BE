package com.example.bigfood.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bigfood.entity.Food;

import jakarta.transaction.Transactional;

public interface FoodRepository extends JpaRepository<Food, String> {
    @Query("""
                SELECT f
                FROM Food f
                JOIN f.category fc
                WHERE fc.restaurant.userId = :userId AND f.deleted = false
            """)
    List<Food> findAllByRestaurantUserId(@Param("userId") String userId);

    long countByCategory_IdAndAvailableTrue(String categoryId);
    long countByCategoryId(String categoryId);

    @Modifying
    @Transactional
    @Query("UPDATE Food f SET f.sold = f.sold + :amount WHERE f.id = :foodId")
    int increaseCountById(@Param("foodId") String foodId, @Param("amount") int amount);
}
