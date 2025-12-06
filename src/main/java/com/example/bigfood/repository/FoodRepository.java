package com.example.bigfood.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bigfood.entity.Food;

public interface FoodRepository extends JpaRepository<Food, String> {
    @Query("""
                SELECT f
                FROM Food f
                JOIN f.category fc
                WHERE fc.restaurant.userId = :userId AND f.isDeleted = false
            """)
    List<Food> findAllByRestaurantUserId(@Param("userId") String userId);

    long countByCategory_IdAndIsAvailableTrue(String categoryId);
    long countByCategoryId(String categoryId);
}
