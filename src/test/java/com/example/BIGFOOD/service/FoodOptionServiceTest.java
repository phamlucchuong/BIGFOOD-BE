package com.example.BIGFOOD.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.bigfood.repository.FoodOptionRepository;
import com.example.bigfood.service.FoodOptionService;

@ExtendWith(MockitoExtension.class)
public class FoodOptionServiceTest {
    @Mock
    private FoodOptionRepository foodOptionRepository;

    @InjectMocks
    private FoodOptionService foodOptionService;

    @Test
    void testGetByFoodId() {
        // Test implementation goes here
    }
}
