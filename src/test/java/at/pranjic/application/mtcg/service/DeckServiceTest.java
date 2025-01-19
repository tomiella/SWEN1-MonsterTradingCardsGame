package at.pranjic.application.mtcg.service;

import at.pranjic.application.mtcg.entity.Card;
import at.pranjic.application.mtcg.entity.User;
import at.pranjic.application.mtcg.repository.CardRepository;
import at.pranjic.application.mtcg.repository.DeckRepository;
import at.pranjic.application.mtcg.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeckServiceTest {
    private DeckRepository deckRepository;
    private CardRepository cardRepository;
    private UserRepository userRepository;
    private DeckService deckService;

    private User user;
    private Card card1, card2, card3, card4;

    @BeforeEach
    void setUp() {
        deckRepository = mock(DeckRepository.class);
        cardRepository = mock(CardRepository.class);
        userRepository = mock(UserRepository.class);

        deckService = new DeckService(deckRepository, cardRepository, userRepository);

        user = new User(1, "testUser", "password");

        card1 = new Card();
        card1.setId("card1");
        card2 = new Card();
        card2.setId("card2");
        card3 = new Card();
        card3.setId("card3");
        card4 = new Card();
        card4.setId("card4");
    }

    @Test
    void testConfigureDeck_Success() {
        List<String> newDeckIds = List.of("card1", "card2", "card3", "card4");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(deckRepository.getDeckByUserId(user.getId())).thenReturn(List.of(card1, card2));

        // Mock card ownership check to return true for all cards
        when(cardRepository.isCardOwnedByUser(user.getId(), "card1")).thenReturn(true);
        when(cardRepository.isCardOwnedByUser(user.getId(), "card2")).thenReturn(true);
        when(cardRepository.isCardOwnedByUser(user.getId(), "card3")).thenReturn(true);
        when(cardRepository.isCardOwnedByUser(user.getId(), "card4")).thenReturn(true);

        deckService.configureDeck("testUser", newDeckIds);

        verify(deckRepository, times(1)).addCardToDeck(user.getId(), "card1");
        verify(deckRepository, times(1)).addCardToDeck(user.getId(), "card2");
        verify(deckRepository, times(1)).addCardToDeck(user.getId(), "card3");
        verify(deckRepository, times(1)).addCardToDeck(user.getId(), "card4");

        verify(deckRepository, times(1)).removeCardFromDeck(user.getId(), "card1");
        verify(deckRepository, times(1)).removeCardFromDeck(user.getId(), "card2");
    }

    @Test
    void testConfigureDeck_Failure_InvalidDeckSize() {
        List<String> invalidDeckIds = List.of("card1", "card2");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                deckService.configureDeck("testUser", invalidDeckIds));

        assertEquals("Deck IDs must contain exactly 4 elements", exception.getMessage());
    }

    @Test
    void testAddCardToDeck_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(cardRepository.isCardOwnedByUser(user.getId(), "card1")).thenReturn(true);

        deckService.addCardToDeck("testUser", "card1");

        verify(deckRepository, times(1)).addCardToDeck(user.getId(), "card1");
    }

    @Test
    void testAddCardToDeck_Failure_UserNotFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                deckService.addCardToDeck("testUser", "card1"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testAddCardToDeck_Failure_CardNotOwned() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(cardRepository.isCardOwnedByUser(user.getId(), "card1")).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                deckService.addCardToDeck("testUser", "card1"));

        assertEquals("Card card1 is not owned by user 1", exception.getMessage());
    }

    @Test
    void testRemoveCardFromDeck_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        deckService.removeCardFromDeck("testUser", "card1");

        verify(deckRepository, times(1)).removeCardFromDeck(user.getId(), "card1");
    }

    @Test
    void testRemoveCardFromDeck_Failure_UserNotFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                deckService.removeCardFromDeck("testUser", "card1"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testGetDeck_Success() {
        List<Card> cards = List.of(card1, card2, card3, card4);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(deckRepository.getDeckByUserId(user.getId())).thenReturn(cards);

        List<Card> result = deckService.getDeck("testUser");

        assertEquals(4, result.size());
        assertEquals("card1", result.get(0).getId());
        assertEquals("card2", result.get(1).getId());
    }

    @Test
    void testGetDeck_Failure_UserNotFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                deckService.getDeck("testUser"));

        assertEquals("User not found", exception.getMessage());
    }
}
