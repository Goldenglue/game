package database.executor;

import java.sql.*;
import java.util.function.Consumer;

public class Executor {
    private Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public <T> T execQuery(String query, ResultHandler<T> handler) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            try (ResultSet result = stmt.getResultSet()) {
                return handler.handle(result);
            }
        }
    }

    public int execPreparedUpdate(String query, Consumer<PreparedStatement> consumer) {
        int key = 0;
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            consumer.accept(ps);
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                key = generatedKeys.next() ? generatedKeys.getInt(1) : 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return key;
    }

    public <T> T execPreparedQuery(String query, ResultHandler<T> handler, Consumer<PreparedStatement> consumer) {
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            consumer.accept(ps);
            try (ResultSet result = ps.executeQuery()) {
                return handler.handle(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
