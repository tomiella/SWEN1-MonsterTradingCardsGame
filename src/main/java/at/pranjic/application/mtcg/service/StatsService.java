package at.pranjic.application.mtcg.service;

import at.pranjic.application.mtcg.entity.UserStats;
import at.pranjic.application.mtcg.repository.StatsRepository;
import at.pranjic.application.mtcg.repository.UserRepository;

public class StatsService {
    private final StatsRepository statsRepository;
    private final UserRepository userRepository;

    public StatsService(StatsRepository statsRepository, UserRepository userRepository) {
        this.statsRepository = statsRepository;
        this.userRepository = userRepository;
    }

    public UserStats getStats(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return statsRepository.getUserStats(user.getId());
    }
}