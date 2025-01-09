package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.Card;
import at.pranjic.application.mtcg.entity.CardInfo;
import at.pranjic.application.mtcg.repository.CardRepository;
import at.pranjic.application.mtcg.data.ConnectionPool;

import java.sql.*;
        import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardDbRepository implements CardRepository {
    private final ConnectionPool connectionPool;

    public CardDbRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void save(Card card) {
        String sql = "INSERT INTO cards (id, name, damage, element_type, card_type) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, card.getId());
            stmt.setString(2, card.getName());
            stmt.setInt(3, card.getDamage());
            stmt.setString(4, card.getElementType());
            stmt.setString(5, card.getCardType());
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving card", e);
        }
    }

    @Override
    public Optional<Card> findById(String cardId) {
        String sql = "SELECT * FROM cards WHERE id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cardId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToCard(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Card> findAllByUserId(long userId) {
        String sql = "SELECT c.id, c.name, c.damage, c.element_type, c.card_type " +
                "FROM user_cards uc " +
                "JOIN cards c ON uc.card_id = c.id " +
                "WHERE uc.user_id = ?";
        List<Card> cards = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cards.add(mapResultSetToCard(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching cards for user", e);
        }
        return cards;
    }

    @Override
    public List<Card> findAll() {
        String sql = "SELECT * FROM cards";
        List<Card> cards = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cards.add(mapResultSetToCard(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    @Override
    public boolean isCardOwnedByUser(long userId, String cardId) {
        String sql = "SELECT COUNT(*) AS count FROM user_cards WHERE user_id = ? AND card_id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setString(2, cardId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking card ownership", e);
        }
        return false;
    }

    public static Card mapResultSetToCard(ResultSet rs) throws SQLException {
        Card cardObj = new Card();
        cardObj.setId(rs.getString("id"));
        cardObj.setDamage(rs.getInt("damage"));
        cardObj.setInfo(CardInfo.fromDisplayName(rs.getString("name")));
        return cardObj;
    }
}

