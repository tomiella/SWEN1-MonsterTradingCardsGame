package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.UserStats;

import java.util.List;

public interface ScoreboardRepository {
    List<UserStats> getScoreboard();
}
