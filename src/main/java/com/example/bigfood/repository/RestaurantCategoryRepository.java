package com.example.bigfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.example.bigfood.entity.RestaurantCategory;

@Repository
public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory , String> {
    boolean existsByName(String name);
}