package com.example.bigfood.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "finances")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Finance {
    @Id
    @Column(name = "order_id", columnDefinition = "CHAR(36)")
    String orderId;

    @Column(name = "total_amount", columnDefinition = "DECIMAL(10,2) check (total_amount >= 0)")
    double totalAmount;

    @Column(name = "admin_profit", columnDefinition = "DECIMAL(10,2) check (admin_profit >= 0)")
    double adminProfit;

    @Column(name = "restaurant_profit", columnDefinition = "DECIMAL(10,2) check (restaurant_profit >= 0)")
    double restaurantProfit;

    @Column(length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED'))")
    String status;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "order_id", columnDefinition = "CHAR(36)")
    Order order;
}