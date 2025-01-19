package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.TradingDeal;
import at.pranjic.application.mtcg.data.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TradingDbRepository implements TradingRepository {
    private final ConnectionPool connectionPool;

    public TradingDbRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void createTradingDeal(TradingDeal tradingDeal) {
        String sql = "INSERT INTO tradings (offered_card_id, requested_card_type, requested_min_damage, user_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tradingDeal.getOfferedCardId());
            stmt.setString(2, tradingDeal.getRequestedCardType());
            stmt.setInt(3, tradingDeal.getRequestedMinDamage());
            stmt.setInt(4, tradingDeal.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating trading deal", e);
        }
    }

    @Override
    public List<TradingDeal> getAllTradingDeals() {
        String sql = "SELECT * FROM tradings";
        List<TradingDeal> deals = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                deals.add(new TradingDeal(
                        rs.getString("id"),
                        rs.getInt("offered_card_id"),
                        rs.getString("requested_card_type"),
                        rs.getInt("requested_min_damage"),
                        rs.getInt("user_id")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all trading deals", e);
        }
        return deals;
    }

    @Override
    public Optional<TradingDeal> getTradingDealById(int tradingDealId) {
        String sql = "SELECT * FROM tradings WHERE id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tradingDealId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new TradingDeal(
                            rs.getString("id"),
                            rs.getInt("offered_card_id"),
                            rs.getString("requested_card_type"),
                            rs.getInt("requested_min_damage"),
                            rs.getInt("user_id")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching trading deal by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public void deleteTradingDeal(int tradingDealId) {
        String sql = "DELETE FROM tradings WHERE id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tradingDealId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting trading deal", e);
        }
    }
}