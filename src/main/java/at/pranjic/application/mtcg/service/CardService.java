package at.pranjic.application.mtcg.service;

import at.pranjic.application.mtcg.entity.Card;
import at.pranjic.application.mtcg.repository.CardRepository;

import java.util.List;
import java.util.Optional;

public class CardService {
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public void addCard(Card card) {
        cardRepository.save(card);
    }

    public Optional<Card> getCardById(int cardId) {
        return cardRepository.findById(cardId);
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

}
