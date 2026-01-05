package com.example.bigfood.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bigfood.dto.response.ApiResponse;
import com.example.bigfood.dto.response.FinanceResponse;
import com.example.bigfood.service.FinanceService;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/finances")
public class FinanceController {
    private FinanceService financeService;

    // @GetMapping("/summary")
    // @PostAuthorize("hasRole('ADMIN')")
    // public ApiResponse<FinanceResponse> getFinanceSummary() {
    //     return ApiResponse.<FinanceResponse>builder()
    //         .results(financeService.getFinanceSummary())
    //         .message("Finance summary retrieved successfully")
    //         .build();
    // }
    
}
