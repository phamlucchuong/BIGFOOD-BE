package com.example.bigfood.service;

import org.springframework.stereotype.Service;

import com.example.bigfood.entity.Food;
import com.example.bigfood.entity.FoodOption;
import com.example.bigfood.repository.FoodOptionRepository;

@Service
public class FoodOptionService {
    private FoodOptionRepository foodOptionRepository;

    protected FoodOptionService(FoodOptionRepository foodOptionRepository) {
        this.foodOptionRepository = foodOptionRepository;
    }

    protected FoodOption getByFoodId(String foodId) {
        return foodOptionRepository.findAllByFood_Id(foodId);
    }

    public FoodOption createFoodOption(String name , double price ,boolean flag  ,Food food){
          FoodOption foodOption = FoodOption.builder()
                                .name(name)
                                .price(price)
                                .defaultPrice((flag) ? true : false)
                                .food(food)
                                .build();
          return foodOptionRepository.save(foodOption);
    }
}
