package com.example.bigfood.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bigfood.entity.Role;

public interface RoleRepository  extends JpaRepository<Role , String>{
    
    @EntityGraph(attributePaths = {"permission"})
    Optional<Role> findByName(String name);
}
