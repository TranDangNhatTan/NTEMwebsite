package com.example.testSpringBoot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "quiz_results")
@Getter
@Setter
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Integer resultId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "quiz_id", nullable = false)
    private Integer quizId;

    @Column(name = "score", nullable = false)
    private Double score;

    @Column(name = "completed_at", nullable = false)
    private java.sql.Timestamp completedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "quiz_id", insertable = false, updatable = false)
    private Quiz quiz;
}
