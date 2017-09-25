package database.services.implementations;

import database.DataSource;
import database.dao.SessionDao;
import database.services.SessionService;

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
