package at.pranjic.application.mtcg.repository;


import at.pranjic.application.mtcg.data.ConnectionPool;
import at.pranjic.application.mtcg.entity.User;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class UserDbRepository implements UserRepository {

    private final static String NEW_USER = "INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?)";

    private final ConnectionPool connectionPool;

    public UserDbRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        System.out.println("here1");
        try (Connection conn = connectionPool.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.execute();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                }
            }
            System.out.println("here2");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCardToUser(long userId, String cardId) {
        String sql = "INSERT INTO user_cards (user_id, card_id) VALUES (?, ?)";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setString(2, cardId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding card to user", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = connectionPool.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = connectionPool.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET coins = ?, elo = ?, games_played = ?, wins = ?, losses = ?, name = ?, bio = ?, image = ? WHERE id = ?";
        try (Connection conn = connectionPool.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.getCoins());
            pstmt.setInt(2, user.getElo());
            pstmt.setInt(3, user.getGames_played());
            pstmt.setInt(4, user.getWins());
            pstmt.setInt(5, user.getLosses());
            pstmt.setString(6, user.getName());
            pstmt.setString(7, user.getBio());
            pstmt.setString(8, user.getImage());
            pstmt.setLong(9, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = connectionPool.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setCoins(rs.getInt("coins"));
        user.setElo(rs.getInt("elo"));
        user.setGames_played(rs.getInt("games_played"));
        user.setWins(rs.getInt("wins"));
        user.setLosses(rs.getInt("losses"));
        user.setName(rs.getString("name"));
        user.setBio(rs.getString("bio"));
        user.setImage(rs.getString("image"));
        // Map other fields as necessary
        return user;
    }
}
