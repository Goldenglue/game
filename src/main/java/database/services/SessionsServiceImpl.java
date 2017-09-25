package database.services;

import database.DataSource;
import database.dao.SessionDao;

public class SessionsServiceImpl implements SessionService {
    private final DataSource source;

    public SessionsServiceImpl(DataSource source) {
        this.source = source;
    }

    @Override
    public void add(String sessionId, int userId) {
        SessionDao dao = new SessionDao(source.getConnection());
        dao.add(sessionId, userId);
    }

    @Override
    public void endSession(String sessionId) {
        SessionDao dao = new SessionDao(source.getConnection());
        dao.remove(sessionId);
    }
}
