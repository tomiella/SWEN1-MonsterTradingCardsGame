package at.pranjic.application.mtcg.service;

import at.pranjic.application.mtcg.entity.User;
import at.pranjic.application.mtcg.entity.UserStats;
import at.pranjic.application.mtcg.repository.StatsRepository;
import at.pranjic.application.mtcg.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatsServiceTest {
    private StatsRepository statsRepository;
    private UserRepository userRepository;
    private StatsService statsService;

    private User user;
    private UserStats userStats;

    @BeforeEach
    void setUp() {
        statsRepository = mock(StatsRepository.class);
        userRepository = mock(UserRepository.class);
        statsService = new StatsService(statsRepository, userRepository);

        user = new User(1, "testUser", "password");
        user.setWins(5);
        user.setLosses(2);
        user.setElo(150);
        userStats = new UserStats("testUser", 150, 5, 2);
    }

    @Test
    void testGetStats_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(statsRepository.getUserStats(user.getId())).thenReturn(userStats);

        UserStats result = statsService.getStats("testUser");

        assertNotNull(result);
        assertEquals(150, result.getElo());
        assertEquals(5, result.getWins());
        assertEquals(2, result.getLosses());

        verify(userRepository, times(1)).findByUsername("testUser");
        verify(statsRepository, times(1)).getUserStats(user.getId());
    }

    @Test
    void testGetStats_UserNotFound() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                statsService.getStats("unknownUser"));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findByUsername("unknownUser");
        verifyNoInteractions(statsRepository);
    }
}