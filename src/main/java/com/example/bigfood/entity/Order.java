package com.example.bigfood.entity;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import com.example.bigfood.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "orderDetails", "user", "restaurant" })
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@DynamicInsert
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "VARCHAR(36)")
    String id;

    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(15) default 'PENDING' check (status in ('PENDING', 'CONFIRMED', 'PREPARING', 'DELIVERING', 'COMPLETED', 'CANCELLED', 'REJECTED'))")
    @Builder.Default
    @Enumerated(EnumType.STRING)
    OrderStatus status = OrderStatus.PENDING;

    @Generated(GenerationTime.INSERT)
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @Generated(GenerationTime.ALWAYS)
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "delivery_address", nullable = false, columnDefinition = "VARCHAR(255)")
    String deliveryAddress;

    @Column(name = "delivery_latitude", nullable = false, columnDefinition = "decimal(10,8)")
    double deliveryLatitude;

    @Column(name = "delivery_longitude", nullable = false, columnDefinition = "decimal(11,8)")
    double deliveryLongitude;

    @Column(name = "delivery_distance", nullable = false, columnDefinition = "decimal(10,2)")
    double deliveryDistance;

    @Column(name = "delivery_fee", nullable = false, columnDefinition = "decimal(10,2)")
    double deliveryFee;

    @Column(name = "total_amount", nullable = false, columnDefinition = "decimal(10,2)")
    double totalAmount;

    @Column(name = "payment_method", nullable = false, columnDefinition = "VARCHAR(50) check (payment_method in ('MOMO', 'BANK', 'CASH'))")
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<OrderDetail> orderDetails;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    Review review;
}
