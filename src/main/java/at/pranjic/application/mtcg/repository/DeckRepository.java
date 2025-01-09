package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.Card;

import java.util.List;

public interface DeckRepository {
    void addCardToDeck(long userId, String cardId);
    void removeCardFromDeck(long userId, String cardId);
    List<Card> getDeckByUserId(long userId);
}
