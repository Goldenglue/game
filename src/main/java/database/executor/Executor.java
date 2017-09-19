package database.executor;

import java.sql.*;

public class Executor {
    private Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public int execUpdate(String update) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeUpdate(update);
    }

    public int execInsertStatement(String query, String... arguments) throws SQLException{
        int key = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < arguments.length; i++) {
                ps.setString(i + 1, arguments[i]);
            }
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            generatedKeys.next();
            key = generatedKeys.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return key;
    }

    public <T> T execQuery(String query, ResultHandler<T> handler) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet result = stmt.getResultSet();

        return handler.handle(result);
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
