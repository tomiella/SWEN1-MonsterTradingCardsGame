package at.pranjic.application.mtcg.service;

import at.pranjic.application.mtcg.entity.Card;
import at.pranjic.application.mtcg.entity.User;
import at.pranjic.application.mtcg.repository.CardRepository;
import at.pranjic.application.mtcg.repository.DeckRepository;
import at.pranjic.application.mtcg.repository.UserRepository;
import at.pranjic.application.mtcg.service.DeckService;

import java.util.List;

public class DeckService {
    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public DeckService(DeckRepository deckRepository, CardRepository cardRepository, UserRepository userRepository) {
        this.deckRepository = deckRepository;
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    public void configureDeck(String username, List<String> ids) {
        if (ids.size() != 4) {
            throw new IllegalArgumentException("Deck IDs must contain exactly 4 elements");
        }

        List<Card> currentDeck = getDeck(username);

        ids.forEach(id -> {
            addCardToDeck(username, id);
        });

        currentDeck.forEach(card -> {
            removeCardFromDeck(username, card.getId());
        });
    }

    public void addCardToDeck(String username, String cardId) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!cardRepository.isCardOwnedByUser(user.getId(), cardId)) {
            throw new IllegalArgumentException("Card " + cardId + " is not owned by user " + user.getId());
        }
        deckRepository.addCardToDeck(user.getId(), cardId);
    }

    public void removeCardFromDeck(String username, String cardId) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        deckRepository.removeCardFromDeck(user.getId(), cardId);
    }

    public List<Card> getDeck(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return deckRepository.getDeckByUserId(user.getId());
    }
}

