package com.example.bigfood.entity;


import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "restaurant_categories")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantCategory {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    String id;
    String name;
    
    @ManyToMany(mappedBy = "restaurantCategories")
    List<Restaurant> restaurants;
}
