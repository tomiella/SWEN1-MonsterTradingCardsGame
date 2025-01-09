package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.UserStats;
import at.pranjic.application.mtcg.repository.ScoreboardRepository;
import at.pranjic.application.mtcg.data.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardDbRepository implements ScoreboardRepository {
    private final ConnectionPool connectionPool;

    public ScoreboardDbRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public List<UserStats> getScoreboard() {
        String sql = "SELECT username, elo, wins, losses " +
                "FROM users " +
                "ORDER BY elo DESC";
        List<UserStats> scoreboard = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                scoreboard.add(new UserStats(
                        rs.getString("username"),
                        rs.getInt("elo"),
                        rs.getInt("wins"),
                        rs.getInt("losses")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching scoreboard", e);
        }
        return scoreboard;
    }
}

