package com.example.bigfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bigfood.entity.Role;

public interface RoleRepository  extends JpaRepository<Role , String>{
    
}
