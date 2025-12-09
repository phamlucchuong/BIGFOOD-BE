package com.example.bigfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bigfood.entity.FoodCategory;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, String> {

    
    
}
