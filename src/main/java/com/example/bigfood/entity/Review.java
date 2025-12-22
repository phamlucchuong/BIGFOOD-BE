package com.example.bigfood.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Generated;

import com.example.bigfood.enums.Sentiment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@DynamicInsert
public class Review {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
    String id;

    @Column(name = "rating", nullable = false, columnDefinition = "INT CHECK (rating >= 1 AND rating <= 5)")
    int rating;
    String reviewText;

    @Column(name = "last_update_at")
    @Generated(org.hibernate.annotations.GenerationTime.ALWAYS)
    LocalDateTime lastUpdateAt;


    @Enumerated(jakarta.persistence.EnumType.STRING)
    @Column(name = "sentiment", columnDefinition = "VARCHAR(20) DEFAULT 'NEUTRAL' check (sentiment IN ('POSITIVE', 'NEUTRAL', 'NEGATIVE'))")
    Sentiment sentiment;

    String replyText;
    @Column(name = "reply_at")
    LocalDateTime replyAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true, columnDefinition = "VARCHAR(36)", nullable = false)
    Order order;
}
