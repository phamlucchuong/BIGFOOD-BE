package com.example.BIGFOOD.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BIGFOOD.entity.User;




@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findById(String username);
    boolean findByEmail(String email);
}
