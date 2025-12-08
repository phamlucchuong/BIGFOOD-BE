package com.example.bigfood.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"order", "food"})
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "VARCHAR(36)")
    String id;

    @Column(name = "food_name", nullable = false, columnDefinition = "VARCHAR(100)")
    String foodName;

    @Column(name = "quantity", nullable = false, columnDefinition = "INT check (quantity > 0)")
    int quantity;

    @Column(name = "unit_price", nullable = false, columnDefinition = "DECIMAL(10,2) check (unit_price >= 0)")
    double unitPrice;

    @Column(name = "total_price", nullable = false, columnDefinition = "DECIMAL(10,2) check (total_price >= 0)")
    double totalPrice;

    @Column(name = "notes", columnDefinition = "TEXT")
    String notes;

    @ManyToOne
    @JoinColumn(name = "order_id", columnDefinition = "VARCHAR(36)", nullable = false)
    Order order;

    @ManyToOne
    @JoinColumn(name = "food_id", columnDefinition = "VARCHAR(36)", nullable = false)
    Food food;
}
