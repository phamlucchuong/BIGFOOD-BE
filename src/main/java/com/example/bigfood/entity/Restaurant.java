package com.example.bigfood.entity;

import java.util.Set;


import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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

    // Cột geometry để query khoảng cách
    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(columnDefinition = "point SRID 4326 NOT NULL")
    Point location;
    
    @Column(name = "banner_id")
    String bannerId;
    
    @Column(name = "license_id")
    String licenseId;
    
    @Column(name = "is_approved", columnDefinition = "BOOLEAN DEFAULT FALSE")
    Boolean isApproved;


    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "restaurant_has_categories", 
        joinColumns = @JoinColumn(name = "restaurant_id", columnDefinition = "CHAR(36)"),
        inverseJoinColumns = @JoinColumn(name = "restaurant_category_id", columnDefinition = "CHAR(36)")
    )
    Set<RestaurantCategory> restaurantCategories;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<FoodCategory> foodCategories;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    User user;


    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    Set<Order> orders;

    // Đồng bộ Point từ lat/lng trước khi lưu
    @PrePersist
    @PreUpdate
    private void syncLocation() {
        if (location == null || location.getX() != longitude || location.getY() != latitude) {
            // Dùng GeometryFactory từ nơi khác truyền vào hoặc tự tạo tạm (SRID 4326)
            location = new org.locationtech.jts.geom.GeometryFactory(
                    new org.locationtech.jts.geom.PrecisionModel(), 4326
            ).createPoint(new org.locationtech.jts.geom.Coordinate(longitude, latitude));
            location.setSRID(4326);
        }
    }
}