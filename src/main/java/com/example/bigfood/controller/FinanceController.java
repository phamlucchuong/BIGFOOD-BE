package com.example.bigfood.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bigfood.dto.response.ApiResponse;
import com.example.bigfood.service.FinanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/finances")
public class FinanceController {
    private FinanceService financeService;

    @GetMapping("/chart")
    public ApiResponse<?> getMethodName(@RequestParam String param) {
        return new ApiResponse<>();
    }
    
}
