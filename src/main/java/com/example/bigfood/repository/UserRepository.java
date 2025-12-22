package com.example.bigfood.repository;



import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bigfood.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    long countByCreatedAtBetweenAndDeletedFalse(LocalDateTime startTime, LocalDateTime endTime);
    Optional<User> findByEmailAndDeletedFalse(String emailRequest);
    Optional<User> findByIdAndDeletedFalse(String id);
    List<User> findByRolesIn(List<String> roles);
}
