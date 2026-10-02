package com.clyo.backend.repository;

import com.clyo.backend.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScoreRepository extends JpaRepository<Score, UUID> {

    // Find scores with points greater than the given minimum value
    List<Score> findByPointGreaterThan(Integer minValue);

    // Find all scores ordered from newest to oldest
    List<Score> findAllByOrderByCreatedAtDesc();

    // Find the highest scores
    @Query("SELECT s FROM Score s ORDER BY s.point DESC")
    List<Score> findTopScores(Integer limit);
}