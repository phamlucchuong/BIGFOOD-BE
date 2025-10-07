package com.example.bigfood.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "permission")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {
    @Id
    private String name ;
    private String decription;
}
