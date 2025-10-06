package com.example.bigfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bigfood.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission , String> {
    
}
