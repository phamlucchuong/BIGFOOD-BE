package com.example.bigfood.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.bigfood.entity.Finance;
import com.example.bigfood.entity.Order;

public interface FinanceRepository extends JpaRepository<Finance, String> {

    Optional<Finance> findByOrder(Order order);
    
}
