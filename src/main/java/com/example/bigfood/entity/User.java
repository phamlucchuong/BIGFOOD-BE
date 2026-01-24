package com.example.bigfood.entity;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Generated;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@DynamicInsert
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    String id;
    String name;
    String email;
    String password;
    String phone;
    @Column(name = "image_id")
    String imageId;


    @Column(name = "created_at", updatable = false)
    @Generated(org.hibernate.annotations.GenerationTime.INSERT)
    LocalDateTime createdAt;

    @Column(name = "is_deleted")
    @Generated(org.hibernate.annotations.GenerationTime.ALWAYS)
    boolean deleted;

    @ManyToMany
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_name"))
    Set<Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    Restaurant restaurant;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    Set<Order> orders;
    
}
