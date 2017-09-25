package database.services;

import database.dao.SessionDao;

public class SessionsService implements Service {
    public void add(String sessionId, int userId) {
        SessionDao dao = new SessionDao(source.getConnection());
        dao.add(sessionId, userId);
    }

    public void endSession(String sessionId) {
        SessionDao dao = new SessionDao(source.getConnection());
        dao.remove(sessionId);
    }
}
