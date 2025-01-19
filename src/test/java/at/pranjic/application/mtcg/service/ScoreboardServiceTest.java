package at.pranjic.application.mtcg.service;

import at.pranjic.application.mtcg.entity.UserStats;
import at.pranjic.application.mtcg.repository.ScoreboardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScoreboardServiceTest {
    private ScoreboardRepository scoreboardRepository;
    private ScoreboardService scoreboardService;

    @BeforeEach
    void setUp() {
        scoreboardRepository = mock(ScoreboardRepository.class);
        scoreboardService = new ScoreboardService(scoreboardRepository);
    }

    @Test
    void testGetScoreboard_Success() {
        List<UserStats> mockScoreboard = List.of(
                new UserStats("player1", 150, 5, 3),
                new UserStats("player2", 120, 3, 4)
        );

        when(scoreboardRepository.getScoreboard()).thenReturn(mockScoreboard);

        List<UserStats> result = scoreboardService.getScoreboard();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(150, result.get(0).getElo());
        assertEquals(5, result.get(0).getWins());
        assertEquals(3, result.get(0).getLosses());

        verify(scoreboardRepository, times(1)).getScoreboard();
    }

    @Test
    void testGetScoreboard_EmptyResult() {
        when(scoreboardRepository.getScoreboard()).thenReturn(List.of());

        List<UserStats> result = scoreboardService.getScoreboard();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(scoreboardRepository, times(1)).getScoreboard();
    }
}
