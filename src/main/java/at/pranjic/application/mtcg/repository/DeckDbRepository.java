package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.Card;
import at.pranjic.application.mtcg.data.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static at.pranjic.application.mtcg.repository.CardDbRepository.mapResultSetToCard;

public class DeckDbRepository implements DeckRepository {
    private final ConnectionPool connectionPool;

    public DeckDbRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void addCardToDeck(long userId, String cardId) {
        String sql = "UPDATE user_cards SET is_in_deck = TRUE WHERE user_id = ? AND card_id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setString(2, cardId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding card to deck", e);
        }
    }

    @Override
    public void removeCardFromDeck(long userId, String cardId) {
        String sql = "UPDATE user_cards SET is_in_deck = FALSE WHERE user_id = ? AND card_id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setString(2, cardId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing card from deck", e);
        }
    }

    @Override
    public List<Card> getDeckByUserId(long userId) {
        String sql = "SELECT c.id, c.name, c.damage, c.element_type, c.card_type " +
                "FROM user_cards uc " +
                "JOIN cards c ON uc.card_id = c.id " +
                "WHERE uc.user_id = ? AND uc.is_in_deck = TRUE";
        List<Card> deck = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    deck.add(mapResultSetToCard(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving deck for user", e);
        }
        return deck;
    }
}

