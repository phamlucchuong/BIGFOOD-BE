package com.example.BIGFOOD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BIGFOOD.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission , String> {
    
}
