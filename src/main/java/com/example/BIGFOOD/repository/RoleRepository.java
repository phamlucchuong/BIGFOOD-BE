package com.example.BIGFOOD.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BIGFOOD.entity.Role;

public interface RoleRepository  extends JpaRepository<Role , String>{
    
}
