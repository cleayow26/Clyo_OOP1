package com.clyo.backend.service;

import com.clyo.backend.model.Score;
import com.clyo.backend.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    // Create a new score
    public Score createScore(Score score) {
        return scoreRepository.save(score);
    }

    // Get a score by its scoreId
    public Optional<Score> getScoreByID(UUID scoreId) {
        return scoreRepository.findById(scoreId);
    }

    // Q1: Get all scores
    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }

    // Q2: Get all scores from newest to oldest
    public List<Score> getRecentScores() {
        return scoreRepository.findAllByOrderByCreatedAtDesc();
    }

    // Q3: Get scores with points greater than the minimum value
    public List<Score> getScoreAboveValue(Integer minValue) {
        return scoreRepository.findByPointGreaterThan(minValue);
    }

    // Q4: Get the top scores based on the requested limit
    public List<Score> getLeaderboard(Integer limit) {
        return scoreRepository.findTopScores(limit)
                .stream()
                .limit(limit)
                .toList();
    }

    // Q5: Delete a score by its scoreId
    public void deleteScore(UUID scoreId) {
        Score score = scoreRepository.findById(scoreId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Score with ID " + scoreId + " was not found"
                        ));

        scoreRepository.delete(score);
    }
}