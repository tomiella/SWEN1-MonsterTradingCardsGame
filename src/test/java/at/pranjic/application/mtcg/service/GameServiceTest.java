package at.pranjic.application.mtcg.service;

import at.pranjic.application.mtcg.entity.Battle;
import at.pranjic.application.mtcg.entity.Card;
import at.pranjic.application.mtcg.entity.CardInfo;
import at.pranjic.application.mtcg.entity.User;
import at.pranjic.application.mtcg.repository.CardRepository;
import at.pranjic.application.mtcg.repository.DeckRepository;
import at.pranjic.application.mtcg.repository.GameRepository;
import at.pranjic.application.mtcg.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {
    private UserRepository userRepository;
    private DeckRepository deckRepository;
    private GameRepository gameRepository;
    private CardRepository cardRepository;
    private GameService gameService;

    private User user1;
    private User user2;
    private Card card1;
    private Card card2;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        deckRepository = mock(DeckRepository.class);
        gameRepository = mock(GameRepository.class);
        cardRepository = mock(CardRepository.class);

        gameService = new GameService(userRepository, deckRepository, gameRepository);

        user1 = new User(1, "player1", "password");
        user2 = new User(2, "player2", "password");

        card1 = new Card();
        card1.setId("1");
        card1.setInfo(CardInfo.DRAGON);
        card1.setDamage(50);

        card2 = new Card();
        card2.setId("2");
        card2.setInfo(CardInfo.FIRE_GOBLIN);
        card2.setDamage(40);
    }

    @Test
    void testStartBattle_Success() {
        when(userRepository.findByUsername("player1")).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername("player2")).thenReturn(Optional.of(user2));

        new Thread(() -> gameService.startBattle("player1")).start();
        String result = gameService.startBattle("player2");

        assertNotNull(result);
        assertTrue(result.contains("battle started"));
    }

    @Test
    void testStartBattle_UserAlreadyInGame() {
        when(userRepository.findByUsername("player1")).thenReturn(Optional.of(user1));

        new Thread(() -> gameService.startBattle("player1")).start();

        Exception exception = assertThrows(RuntimeException.class, () -> gameService.startBattle("player1"));

        assertEquals("User already started", exception.getMessage());
    }

    @Test
    void testStartBattle_UserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> gameService.startBattle("unknown"));

        assertEquals("User not valid", exception.getMessage());
    }

    @Test
    void testExecuteBattle_Player1Wins() {
        List<Card> deck1 = List.of(card1);
        List<Card> deck2 = List.of(card2);

        when(deckRepository.getDeckByUserId(user1.getId())).thenReturn(deck1);
        when(deckRepository.getDeckByUserId(user2.getId())).thenReturn(deck2);
        when(userRepository.findByUsername("player1")).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername("player2")).thenReturn(Optional.of(user2));

        new Thread(() -> gameService.startBattle("player1")).start();
        gameService.startBattle("player2");

        assertEquals(user1.getId(), gameService.getWinner(card1, card2).getId());

        ArgumentCaptor<Battle> captor = ArgumentCaptor.forClass(Battle.class);
        verify(gameRepository, times(1)).createBattle(captor.capture());

        Battle savedBattle = captor.getValue();
        assertEquals(user1.getId(), savedBattle.getWinnerId());
    }

    @Test
    void testSpellDamage_FireBeatsNormal() {
        card1.setInfo(CardInfo.FIRE_SPELL);
        card2.setInfo(CardInfo.REGULAR_SPELL);

        double damage = gameService.spellDamage(card1, card2.getElementType());

        assertEquals(100, damage);
    }

    @Test
    void testSpellDamage_WaterBeatsFire() {
        card1.setInfo(CardInfo.WATER_SPELL);
        card2.setInfo(CardInfo.FIRE_SPELL);

        double damage = gameService.spellDamage(card1, card2.getElementType());

        assertEquals(100, damage);
    }

    @Test
    void testSpellDamage_NoEffect() {
        card1.setInfo(CardInfo.WATER_SPELL);
        card2.setInfo(CardInfo.WATER_SPELL);

        double damage = gameService.spellDamage(card1, card2.getElementType());

        assertEquals(50, damage);
    }

    @Test
    void testDamageExceptions_GoblinVsDragon() {
        card1.setInfo(CardInfo.DRAGON);
        card2.setInfo(CardInfo.FIRE_GOBLIN);

        assertFalse(gameService.damageExceptions(card2, card1));
    }

    @Test
    void testDamageExceptions_OrkVsWizard() {
        card1.setInfo(CardInfo.WIZARD);
        card2.setInfo(CardInfo.ORK);

        assertFalse(gameService.damageExceptions(card2, card1));
    }
}
