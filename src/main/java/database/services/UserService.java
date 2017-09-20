package database.services;

import database.dao.UserDao;
import database.pojos.User;

import java.sql.SQLException;

public class UserService implements Service {

    public int addNewUser(String username, String password) throws SQLException {
        User user = new User(username, password);
        UserDao dao = new UserDao(source.getConnection());
        return dao.add(user);
    }

    public User getByUsername(String username) {
        UserDao dao = new UserDao(source.getConnection());
        return dao.getByUsername(username);
    }
}
