package at.pranjic.application.mtcg.service;

import at.pranjic.application.mtcg.entity.UserStats;
import at.pranjic.application.mtcg.repository.ScoreboardRepository;

import java.util.List;

public class ScoreboardService {
    private final ScoreboardRepository scoreboardRepository;

    public ScoreboardService(ScoreboardRepository scoreboardRepository) {
        this.scoreboardRepository = scoreboardRepository;
    }

    public List<UserStats> getScoreboard() {
        return scoreboardRepository.getScoreboard();
    }
}

