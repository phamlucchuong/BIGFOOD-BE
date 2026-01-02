package com.example.bigfood.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "food_options")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class FoodOption {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    String id;
    String name;
    @Column(name = "price", columnDefinition = "decimal(10, 2) not null check (price >= 0)")
    double price;

    @Column(name = "is_default_price", nullable = false, columnDefinition = "boolean default false")
    boolean defaultPrice;

    @ManyToOne
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    Food food;
}
