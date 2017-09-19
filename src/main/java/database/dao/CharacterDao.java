package database.dao;

import database.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;

public class CharacterDao {
    private final Executor executor;

    public CharacterDao(Connection connection) {
        this.executor = new Executor(connection);
    }

    public long getCharacterID(Long userID) throws SQLException {
        return executor.execPreparedQuery("select id from characters where ownerID = ?", result -> {
            result.next();
            return result.getLong(1);
        });
    }
}
