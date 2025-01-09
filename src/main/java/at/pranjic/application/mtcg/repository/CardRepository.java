package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.Card;

import java.util.List;
import java.util.Optional;

public interface CardRepository {
    void save(Card card);
    Optional<Card> findById(int cardId);
    List<Card> findAll();
}

