package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.Package;
import at.pranjic.application.mtcg.data.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PackageDbRepository implements PackageRepository {
    private final ConnectionPool connectionPool;

    public PackageDbRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void save(Package pkg, List<String> cardIds) {
        String packageSql = "INSERT INTO packages DEFAULT VALUES RETURNING id";
        String packageCardsSql = "INSERT INTO package_cards (package_id, card_id) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement packageStmt = null;
        PreparedStatement packageCardsStmt = null;

        try {
            conn = connectionPool.getConnection();
            conn.setAutoCommit(false);

            packageStmt = conn.prepareStatement(packageSql);
            ResultSet rs = packageStmt.executeQuery();
            if (rs.next()) {
                pkg.setId(rs.getInt(1));
            }

            packageCardsStmt = conn.prepareStatement(packageCardsSql);
            for (String cardId : cardIds) {
                packageCardsStmt.setInt(1, pkg.getId());
                packageCardsStmt.setString(2, cardId);
                packageCardsStmt.addBatch();
            }
            packageCardsStmt.executeBatch();

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            throw new RuntimeException("Error saving package", e);
        } finally {
            try {
                if (packageCardsStmt != null) packageCardsStmt.close();
                if (packageStmt != null) packageStmt.close();
                if (conn != null) conn.setAutoCommit(true);
                connectionPool.releaseConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<Package> findById(int packageId) {
        String sql = "SELECT * FROM packages WHERE id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, packageId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Package(rs.getInt("id")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Package> findAll() {
        String sql = "SELECT * FROM packages";
        List<Package> packages = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                packages.add(new Package(rs.getInt("id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packages;
    }

    @Override
    public void delete(int packageId) {
        String sql = "DELETE FROM packages WHERE id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, packageId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
