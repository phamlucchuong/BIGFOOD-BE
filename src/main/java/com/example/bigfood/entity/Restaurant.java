package com.example.bigfood.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Restaurant {
    @Id
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    String userId;
    
    @Column(name = "restaurant_name")
    String restaurantName;
    
    @Column(name = "address")
    String address;

    @Column(name="name_bank")
    String nameBank;
    
    @Column(name="bank_number")
    String bankNumber;

    @Column(name="bank_account_name")
    String bankAccountName;
    
    double latitude;
    double longitude;
    
    @Column(name = "banner_id")
    String bannerId;
    
    @Column(name = "license_id")
    String licenseId;
    
    @Column(name = "is_approved", columnDefinition = "BOOLEAN DEFAULT FALSE")
    Boolean isApproved;


    @ManyToMany
    @JoinTable(
        name = "restaurant_has_categories", 
        joinColumns = @JoinColumn(name = "restaurant_id", columnDefinition = "CHAR(36)"),
        inverseJoinColumns = @JoinColumn(name = "restaurant_category_id", columnDefinition = "CHAR(36)")
    )
    Set<RestaurantCategory> restaurantCategories;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    Set<FoodCategory> foodCategories;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    Set<Rating> ratings;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    User user;
}