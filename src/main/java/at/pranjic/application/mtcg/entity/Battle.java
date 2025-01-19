package at.pranjic.application.mtcg.entity;

import java.time.LocalDateTime;

public class Battle {
    private int id;
    private Long player1Id;
    private Long player2Id;
    private Long winnerId;
    private String log;
    private LocalDateTime createdAt;

    public Battle() {}

    public Battle(int id, long player1Id, long player2Id, Long winnerId, String log, LocalDateTime createdAt) {
        this.id = id;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.winnerId = winnerId;
        this.log = log;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Long getPlayer1Id() { return player1Id; }
    public void setPlayer1Id(long player1Id) { this.player1Id = player1Id; }

    public Long getPlayer2Id() { return player2Id; }
    public void setPlayer2Id(long player2Id) { this.player2Id = player2Id; }

    public Long getWinnerId() { return winnerId; }
    public void setWinnerId(long winnerId) { this.winnerId = winnerId; }

    public String getLog() { return log; }
    public void setLog(String log) { this.log = log; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}