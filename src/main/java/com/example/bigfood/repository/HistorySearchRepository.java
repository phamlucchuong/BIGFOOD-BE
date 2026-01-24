package com.example.bigfood.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.example.bigfood.entity.HistorySearch;


public interface HistorySearchRepository extends JpaRepository<HistorySearch, String> {
    
    @Modifying
    @Transactional
    void deleteByLastSearchedAtBefore(LocalDateTime cutoffDate);

    Optional<HistorySearch> findByContent(String content);

    List<HistorySearch> findTop20ByOrderByCountDesc();
}
