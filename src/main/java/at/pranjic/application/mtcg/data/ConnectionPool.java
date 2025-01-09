package at.pranjic.application.mtcg.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {
    private final BlockingQueue<Connection> connectionPool;
    private static final String URL = "jdbc:postgresql://localhost:5432/swen1";
    private static final String USERNAME = "swen1";
    private static final String PASSWORD = "swen1";

    public ConnectionPool(int poolSize) {
        // Initialize the blocking queue with the specified pool size
        connectionPool = new ArrayBlockingQueue<>(poolSize);

        // Populate the pool with connections
        for (int i = 0; i < poolSize; i++) {
            try {
                connectionPool.add(createNewConnection());
            } catch (SQLException e) {
                throw new RuntimeException("Error creating connections for the pool", e);
            }
        }
    }

    private Connection createNewConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public Connection getConnection() throws SQLException {
        try {
            Connection connection = connectionPool.take();
            return AutoReleasingConnectionHandler.wrap(connection, this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Thread interrupted while waiting for a connection", e);
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connectionPool.put(connection);
                } else {
                    connectionPool.put(createNewConnection());
                }
            } catch (SQLException | InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Error releasing connection", e);
            }
        }
    }

    public void closeAllConnections() {
        while (!connectionPool.isEmpty()) {
            try {
                Connection connection = connectionPool.poll();
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
