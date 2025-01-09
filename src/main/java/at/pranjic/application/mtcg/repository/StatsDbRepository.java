package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.UserStats;
import at.pranjic.application.mtcg.repository.StatsRepository;
import at.pranjic.application.mtcg.data.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsDbRepository implements StatsRepository {
    private final ConnectionPool connectionPool;

    public StatsDbRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public UserStats getUserStats(long userId) {
        String sql = "SELECT u.username, u.elo, u.wins, u.losses " +
                "FROM users u " +
                "WHERE u.id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UserStats(
                            rs.getString("username"),
                            rs.getInt("elo"),
                            rs.getInt("wins"),
                            rs.getInt("losses")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching user stats", e);
        }
        throw new RuntimeException("User not found");
    }
}

