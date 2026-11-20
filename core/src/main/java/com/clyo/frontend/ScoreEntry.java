
package com.clyo.frontend;

public class ScoreEntry {

    private String scoreId;
    private String playerName;
    private int point;
    private String createdAt;

    public ScoreEntry() {
    }

    public String getScoreId() {
        return scoreId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPoint() {
        return point;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
