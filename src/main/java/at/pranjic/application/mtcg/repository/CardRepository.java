package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.Card;

import java.util.List;
import java.util.Optional;

public interface CardRepository {
    void save(Card card);
    List<Card> findAllByUserId(long userId);
    Optional<Card> findById(String cardId);
    List<Card> findAll();
    boolean isCardOwnedByUser(long userId, String cardId);
}

