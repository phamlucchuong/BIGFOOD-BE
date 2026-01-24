package com.example.bigfood.entity;

import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Generated;

import jakarta.persistence.CascadeType;
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
@Table(name = "food_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@DynamicInsert
public class FoodCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "CHAR(36)")
    String id;

    @Column(name = "name")
    String name;

    @Column(name = "icon_index", columnDefinition = "INT check (icon_index >= 0)")
    int iconIndex;

    @Column(name = "is_deleted")
    @Generated(org.hibernate.annotations.GenerationTime.ALWAYS)
    boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", columnDefinition = "CHAR(36)", nullable = false)
    Restaurant restaurant;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Food> foods;
}