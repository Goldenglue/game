package database.dao;

import database.executor.Executor;
import database.pojos.User;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDao {
    private final Executor executor;

    public UserDao(Connection connection) {
        this.executor = new Executor(connection);
    }

    public long getUserID(String username) throws SQLException {
        return executor.execPreparedQuery("select id from users where username = ?", result -> {
            result.next();
            return result.getLong(1);
        }, username);
    }

    public int add(User user) throws SQLException {
        return executor.execPreparedQuery("insert into users(username, password) values(?,?)", user.getUsername(), user.getPassword());
    }

    public User getByUsername(String username) {
        return executor.execPreparedQuery("select * from users where username = ?", result -> {
            result.next();

            User user = new User();
            user.setID(result.getLong("id"));
            user.setUsername(result.getString("username"));
            user.setPassword(result.getString("password"));
            user.setReputation(result.getInt("reputation"));
            user.setCharacterID(result.getLong("character_id"));

            return user;
        }, username);
    }
}
