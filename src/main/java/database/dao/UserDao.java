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

    public int add(User user) throws SQLException {
        return executor.execPreparedUpdate("insert into users(username, password) values(?,?)",
                ps -> {
                    try {
                        ps.setString(1, user.getUsername());
                        ps.setString(2, user.getPassword());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }

    public User get(String username) throws SQLException {
        return executor.execPreparedQuery("select * from users where username = ?",
                result -> {
                    if (result.next()) {
                        User user = new User();
                        user.setId(result.getInt("id"));
                        user.setUsername(result.getString("username"));
                        user.setPassword(result.getString("password"));
                        user.setRating(result.getInt("rating"));
                        return user;
                    }
                    return null;
                },
                ps -> {
                    try {
                        ps.setString(1, username);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }

    public User getBySession(String sessionId) throws SQLException {
        return executor.execPreparedQuery("select * from users where id = " +
                        "(select user_id from current_sessions where session_id = ?) ",
                result -> {
                    result.next();
                    User user = new User();
                    user.setId(result.getInt("id"));
                    user.setUsername(result.getString("username"));
                    user.setPassword(result.getString("password"));
                    user.setRating(result.getInt("rating"));
                    return user;
                },
                ps -> {
                    try {
                        ps.setString(1, sessionId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }

    public int getRating(String sessionId) throws SQLException {
        return executor.execPreparedQuery("select rating from users where id =" +
                        " (select user_id from current_sessions where session_id = ?)",
                result -> {
                    result.next();
                    return result.getInt(1);
                },
                ps -> {
                    try {
                        ps.setString(1, sessionId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }
}
