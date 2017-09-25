package database.services;

import database.dao.UserDao;
import pojos.User;

public class UserService implements Service {

    public int addNewUser(String username, String password) {
        User user = new User(username, password);
        UserDao dao = new UserDao(source.getConnection());
        return dao.add(user);
    }

    public User getByUsername(String username) {
        UserDao dao = new UserDao(source.getConnection());
        return dao.get(username);
    }

    public int getRating(String sessionId) {
        UserDao dao = new UserDao(source.getConnection());
        return dao.getRating(sessionId);
    }

    public User getBySession(String sessionId) {
        UserDao dao = new UserDao(source.getConnection());
        return dao.getBySession(sessionId);
    }

    public int updateRatingOnWin(int userId) {
        UserDao dao = new UserDao(source.getConnection());
        return dao.updateRatingOnWin(userId);
    }

    public int updateRatingOnLose(int userId) {
        UserDao dao = new UserDao(source.getConnection());
        return dao.updateRatingOnLose(userId);
    }
}
