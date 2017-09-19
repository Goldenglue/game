package database.dao;

import database.executor.Executor;
import database.pojos.Character;

import java.sql.Connection;
import java.sql.SQLException;

public class CharacterDao {
    private final Executor executor;

    public CharacterDao(Connection connection) {
        this.executor = new Executor(connection);
    }

    public int add(Character character) throws SQLException{
        return executor.execInsertStatement("insert into characters(owner_id) values(?)");
    }
}
