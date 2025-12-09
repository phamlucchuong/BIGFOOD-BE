package com.example.bigfood.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;



@Entity
@Table(name = "foods")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    String id;

    String name;
    String description;

    @Column(name = "image_id")
    String imageId;

    @Column(name = "price", columnDefinition = "decimal(10, 2) not null check (price >= 0)")
    double price;

    @Column(name = "count", columnDefinition = "int default 0")
    @Builder.Default
    int count = 0;
    
    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    @Builder.Default
    boolean isDeleted = false;
    
    @Column(name = "is_available", columnDefinition = "boolean default true")
    @Builder.Default
    boolean isAvailable = true;

    // Quan hệ N:1 với FoodCategory (Sở hữu Khóa ngoại)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition = "CHAR(36)", name = "food_category_id", nullable = false)
    FoodCategory category;

    @OneToMany(mappedBy = "food", fetch = FetchType.LAZY)
    Set<OrderDetail> orderDetails;
}