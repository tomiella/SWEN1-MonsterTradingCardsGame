package at.pranjic.application.mtcg.service;

import at.pranjic.application.mtcg.entity.Card;
import at.pranjic.application.mtcg.repository.CardRepository;
import at.pranjic.application.mtcg.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public CardService(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    public void addCard(Card card) {
        cardRepository.save(card);
    }

    public List<Card> getAllCardsByUserId(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return cardRepository.findAllByUserId(user.getId());
    }

    public Optional<Card> getCardById(String cardId) {
        return cardRepository.findById(cardId);
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

}
