package database.dao;

import database.executor.Executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionDao {
    private final Executor executor;

    public SessionDao(Connection connection) {
        this.executor = new Executor(connection);
    }

    public void add(String sessionId, int userId) {
        executor.execPreparedUpdate("insert into current_sessions(session_id, user_id) values(?,?)",
                ps -> {
                    try {
                        ps.setString(1, sessionId);
                        ps.setInt(2, userId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void remove(String sessionId) {
        executor.execPreparedUpdate("delete from current_sessions where session_id = ?",
                ps -> {
                    try {
                        ps.setString(1, sessionId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }
}
