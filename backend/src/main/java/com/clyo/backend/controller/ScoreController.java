package com.clyo.backend.controller;

import com.clyo.backend.model.Score;
import com.clyo.backend.service.ScoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/scores")
@CrossOrigin(origins = "*")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    // GET /api/scores/{scoreId}
    // Get a score based on its scoreId
    @GetMapping("/{scoreId}")
    public ResponseEntity<?> getScoreById(@PathVariable UUID scoreId) {

        Optional<Score> score = scoreService.getScoreByID(scoreId);

        if (score.isPresent()) {
            return ResponseEntity.ok(score.get());
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("{\"error\": \"Score not found\"}");
    }

    // POST /api/scores
    // Create a new score
    @PostMapping
    public ResponseEntity<?> createScore(@RequestBody Score score) {

        try {
            Score newScore = scoreService.createScore(score);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(newScore);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    // Q6: GET /api/scores
    // Get all scores
    @GetMapping
    public ResponseEntity<List<Score>> getAllScores() {

        List<Score> scores = scoreService.getAllScores();

        return ResponseEntity.ok(scores);
    }

    // Q7: GET /api/scores/leaderboard
    // Get top scores
    @GetMapping("/leaderboard")
    public ResponseEntity<List<Score>> getLeaderboardByPoint(
            @RequestParam(defaultValue = "10") Integer limit) {

        List<Score> scores = scoreService.getLeaderboard(limit);

        return ResponseEntity.ok(scores);
    }

    // Q8: GET /api/scores/above/{minValue}
    // Get scores above a certain point value
    @GetMapping("/above/{minValue}")
    public ResponseEntity<List<Score>> getScoresAboveValue(
            @PathVariable Integer minValue) {

        List<Score> scores = scoreService.getScoreAboveValue(minValue);

        return ResponseEntity.ok(scores);
    }

    // Q9: GET /api/scores/recent
    // Get scores ordered from newest to oldest
    @GetMapping("/recent")
    public ResponseEntity<List<Score>> getRecentScores() {

        List<Score> scores = scoreService.getRecentScores();

        return ResponseEntity.ok(scores);
    }

    // Q10: DELETE /api/scores/{scoreId}
    // Delete a score based on its scoreId
    @DeleteMapping("/{scoreId}")
    public ResponseEntity<?> deleteScore(@PathVariable UUID scoreId) {

        try {
            scoreService.deleteScore(scoreId);

            return ResponseEntity.ok(
                    "{\"message\": \"Score deleted successfully\"}"
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}