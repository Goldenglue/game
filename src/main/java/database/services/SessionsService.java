package database.services;

import database.dao.SessionDao;

import java.sql.SQLException;

public class SessionsService implements Service {
    public void add(String sessionId, int userId) throws SQLException {
        SessionDao dao = new SessionDao(source.getConnection());
        dao.add(sessionId, userId);
    }

    public boolean isLoggedIn(String sessionId) throws SQLException {
        SessionDao dao = new SessionDao(source.getConnection());
        return dao.isLoggedIn(sessionId);
    }

    public void endSession(String sessionId) throws SQLException {
        SessionDao dao = new SessionDao(source.getConnection());
        dao.remove(sessionId);
    }
}
