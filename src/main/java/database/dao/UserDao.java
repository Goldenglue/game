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
        return executor.execInsertStatement("insert into users(username, password) values(?,?)", user.getUsername(), user.getPassword());
    }

    public User get(String username) {
        return executor.execPreparedQuery("select * from users where username = ?", result -> {
            User user = null;
            if (result.next()) {
                user = new User();
                user.setId(result.getInt("id"));
                user.setUsername(result.getString("username"));
                user.setPassword(result.getString("password"));
                user.setReputation(result.getInt("reputation"));
            }
            return user;
        }, username);
    }
}
