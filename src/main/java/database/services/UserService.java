package database.services;

import database.dao.UserDao;
import database.pojos.User;

import java.sql.SQLException;

public class UserService implements Service {

    public void addNewUser(String username, String password) throws SQLException {
        UserDao dao = new UserDao(source.getConnection());
        dao.add(new User(username, password));
    }

    public User getByUsername(String username) {
        UserDao dao = new UserDao(source.getConnection());
        return dao.getByUsername(username);
    }
}
