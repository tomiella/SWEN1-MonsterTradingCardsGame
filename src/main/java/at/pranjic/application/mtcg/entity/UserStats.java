package at.pranjic.application.mtcg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserStats {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("Elo")
    private int elo;

    @JsonProperty("Wins")
    private int wins;

    @JsonProperty("Losses")
    private int losses;

    public UserStats(String name, int elo, int wins, int losses) {
        this.name = name;
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public int getElo() {
        return elo;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }
}

