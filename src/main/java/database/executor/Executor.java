package database.executor;

import java.sql.*;

public class Executor {
    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public int execUpdate(String update) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeUpdate(update);
    }

    public <T> T execQuery(String query, ResultHandler<T> handler) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet result = stmt.getResultSet();

        return handler.handle(result);
    }

    public int execPreparedQuery(String query, String... arguments) {
        int rowsUpdated = 0;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            for (int i = 0; i < arguments.length; i++) {
                ps.setString(i + 1, arguments[i]);
            }
            rowsUpdated = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsUpdated;
    }

    public <T> T execPreparedQuery(String query, ResultHandler<T> handler, String... arguments) {
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            for (int i = 0; i < arguments.length; i++) {
                ps.setString(i + 1, arguments[i]);
            }
            ResultSet result = ps.executeQuery();
            return handler.handle(result);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
