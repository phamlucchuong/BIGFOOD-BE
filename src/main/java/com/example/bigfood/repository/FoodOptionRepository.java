package com.example.bigfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bigfood.entity.FoodOption;

public interface FoodOptionRepository extends JpaRepository<FoodOption, String> {

    FoodOption findAllByFood_Id(String foodId);

}
