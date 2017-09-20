package database.dao;

import database.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;

public class SessionDao {
    private final Executor executor;

    public SessionDao(Connection connection) {
        this.executor = new Executor(connection);
    }

    public void add(String sessionId, int userId) throws SQLException {
        executor.execInsertStatement("insert into current_sessions(session_id, user_id) values(?, '" + userId + "'", sessionId);
    }
}
