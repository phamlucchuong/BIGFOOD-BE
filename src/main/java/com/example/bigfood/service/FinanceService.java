package com.example.bigfood.service;

import org.springframework.stereotype.Service;

import com.example.bigfood.entity.Finance;
import com.example.bigfood.entity.Order;
import com.example.bigfood.enums.ErrorCode;
import com.example.bigfood.exception.AppException;
import com.example.bigfood.repository.FinanceRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class FinanceService {
    FinanceRepository financeRepository;

    protected void createFinance(Order order) {
        Double realAmount = order.getTotalAmount() - order.getDeliveryFee();
        Finance finance = Finance.builder()
            .order(order)
            .totalAmount(realAmount)
            .adminProfit(realAmount * 0.2)
            .restaurantProfit(realAmount * 0.8)
            .build();
        financeRepository.save(finance);
    }

    public void completeFinance(Order order) {
        Finance finance = financeRepository.findByOrder(order)
            .orElseThrow(() -> new AppException(ErrorCode.FINANCE_NOT_FOUND));
        finance.setStatus("COMPLETED");
        financeRepository.save(finance);
    }
}
