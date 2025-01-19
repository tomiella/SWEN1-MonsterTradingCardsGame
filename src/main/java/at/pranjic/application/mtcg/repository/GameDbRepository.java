package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.Battle;
import at.pranjic.application.mtcg.data.ConnectionPool;

import java.sql.*;
        import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameDbRepository implements GameRepository {
    private final ConnectionPool connectionPool;

    public GameDbRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void createBattle(Battle battle) {
        String sql = "INSERT INTO battles (player1_id, player2_id, winner_id, log) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, battle.getPlayer1Id());
            stmt.setLong(2, battle.getPlayer2Id());
            stmt.setObject(3, battle.getWinnerId(), java.sql.Types.INTEGER);
            stmt.setString(4, battle.getLog());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    battle.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating battle", e);
        }
    }

    @Override
    public Optional<Battle> getBattleById(int battleId) {
        String sql = "SELECT * FROM battles WHERE id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, battleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBattle(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching battle by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Battle> getAllBattles() {
        String sql = "SELECT * FROM battles ORDER BY created_at DESC";
        List<Battle> battles = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                battles.add(mapResultSetToBattle(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all battles", e);
        }
        return battles;
    }

    @Override
    public List<Battle> getBattlesByUserId(long userId) {
        String sql = "SELECT * FROM battles WHERE player1_id = ? OR player2_id = ?";
        List<Battle> battles = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    battles.add(mapResultSetToBattle(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching battles for user", e);
        }
        return battles;
    }

    private Battle mapResultSetToBattle(ResultSet rs) throws SQLException {
        return new Battle(
                rs.getInt("id"),
                rs.getInt("player1_id"),
                rs.getInt("player2_id"),
                rs.getObject("winner_id") != null ? rs.getLong("winner_id") : null,
                rs.getString("log"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}

