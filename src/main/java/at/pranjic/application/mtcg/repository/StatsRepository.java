package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.UserStats;

public interface StatsRepository {
    UserStats getUserStats(long userId);
}

