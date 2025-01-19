package at.pranjic.application.mtcg.service;

import at.pranjic.application.mtcg.entity.Card;
import at.pranjic.application.mtcg.entity.User;
import at.pranjic.application.mtcg.repository.CardRepository;
import at.pranjic.application.mtcg.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTest {
    private CardRepository cardRepository;
    private UserRepository userRepository;
    private CardService cardService;

    private User user;
    private Card card1, card2;

    @BeforeEach
    void setUp() {
        cardRepository = mock(CardRepository.class);
        userRepository = mock(UserRepository.class);
        cardService = new CardService(cardRepository, userRepository);

        user = new User(1, "testUser", "password");

        card1 = new Card();
        card1.setId("card1");
        card1.setDamage(50);

        card2 = new Card();
        card2.setId("card2");
        card2.setDamage(30);
    }

    @Test
    void testAddCard_Success() {
        cardService.addCard(card1);

        verify(cardRepository, times(1)).save(card1);
    }

    @Test
    void testGetAllCardsByUserId_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(cardRepository.findAllByUserId(user.getId())).thenReturn(List.of(card1, card2));

        List<Card> result = cardService.getAllCardsByUserId("testUser");

        assertEquals(2, result.size());
        assertEquals("card1", result.get(0).getId());
        assertEquals("card2", result.get(1).getId());
    }

    @Test
    void testGetAllCardsByUserId_UserNotFound() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                cardService.getAllCardsByUserId("unknownUser"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testGetCardById_Success() {
        when(cardRepository.findById("card1")).thenReturn(Optional.of(card1));

        Optional<Card> result = cardService.getCardById("card1");

        assertTrue(result.isPresent());
        assertEquals("card1", result.get().getId());
    }

    @Test
    void testGetCardById_NotFound() {
        when(cardRepository.findById("cardX")).thenReturn(Optional.empty());

        Optional<Card> result = cardService.getCardById("cardX");

        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllCards() {
        when(cardRepository.findAll()).thenReturn(List.of(card1, card2));

        List<Card> result = cardService.getAllCards();

        assertEquals(2, result.size());
        assertEquals("card1", result.get(0).getId());
        assertEquals("card2", result.get(1).getId());
    }
}
