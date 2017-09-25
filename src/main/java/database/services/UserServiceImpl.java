package database.services;

import database.DataSource;
import database.dao.UserDao;
import pojos.User;

public class UserServiceImpl implements UserService {
    private final DataSource source;

    public UserServiceImpl(DataSource source) {
        this.source = source;
    }

    @Override
    public int addNewUser(String username, String password) {
        User user = new User(username, password);
        UserDao dao = new UserDao(source.getConnection());
        return dao.add(user);
    }

    @Override
    public User getByUsername(String username) {
        UserDao dao = new UserDao(source.getConnection());
        return dao.get(username);
    }

    @Override
    public int getRating(String sessionId) {
        UserDao dao = new UserDao(source.getConnection());
        return dao.getRating(sessionId);
    }

    @Override
    public User getBySession(String sessionId) {
        UserDao dao = new UserDao(source.getConnection());
        return dao.getBySession(sessionId);
    }

    @Override
    public int updateRatingOnWin(int userId) {
        UserDao dao = new UserDao(source.getConnection());
        return dao.updateRatingOnWin(userId);
    }

    @Override
    public int updateRatingOnLose(int userId) {
        UserDao dao = new UserDao(source.getConnection());
        return dao.updateRatingOnLose(userId);
    }
}
