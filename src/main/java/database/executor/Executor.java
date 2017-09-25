package database.executor;

import java.sql.*;
import java.util.function.Consumer;

public class Executor {
    private Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public int execPreparedUpdate(String query, Consumer<PreparedStatement> consumer) {
        int key = 0;
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            consumer.accept(ps);
            int updatedRows = ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                key = generatedKeys.next() ? generatedKeys.getInt(1) : updatedRows;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return key;
    }

    public <T> T execPreparedQuery(String query, ResultHandler<T> handler, Consumer<PreparedStatement> consumer) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            consumer.accept(ps);
            try (ResultSet result = ps.executeQuery()) {
                connection.commit();
                connection.setAutoCommit(true);
                return handler.handle(result);
            }
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
            return null;
        }
    }
}
