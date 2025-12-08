package com.example.bigfood.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bigfood.entity.Order;


@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUser_Id(String userId);
    
}
