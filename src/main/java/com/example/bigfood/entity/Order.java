package com.example.bigfood.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "VARCHAR(36)")
    String id;

    @Column(name = "status", nullable = false, 
    columnDefinition = "VARCHAR(15) default 'PENDING' check (status in ('PENDING', 'CONFIRMED', 'PREPARING', 'DELIVERING', 'COMPLETED', 'CANCELLED', 'REJECTED'))")
    String status;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP default current_timestamp")
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP default current_timestamp on update current_timestamp")
    LocalDateTime updatedAt;

    @Column(name = "delivery_address", nullable = false, columnDefinition = "VARCHAR(255)")
    String deliveryAddress;

    @Column(name = "delivery_latitude", nullable = false, columnDefinition = "decimal(10,8)")
    double deliveryLatitude;

    @Column(name = "delivery_longitude", nullable = false, columnDefinition = "decimal(11,8)")
    double deliveryLongitude;

    @Column(name = "delivery_fee", nullable = false, columnDefinition = "decimal(10,2)")
    double deliveryFee;

    @Column(name = "total_amount", nullable = false, columnDefinition = "decimal(10,2)")
    double totalAmount;

    @Column(name = "payment_method", nullable = false, 
    columnDefinition = "VARCHAR(50) check (payment_method in ('MOMO', 'BANK', 'CASH_ON_DELIVERY'))")
    String paymentMethod;

    String notes;
    String cancellReason;
    String rejectReason;

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "VARCHAR(36)", nullable = false)
    User user;


    @ManyToOne
    @JoinColumn(name = "restaurant_id", columnDefinition = "VARCHAR(36)", nullable = false)
    Restaurant restaurant;
}
