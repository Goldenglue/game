package database.dao;

import database.executor.Executor;
import database.pojos.Character;
import database.pojos.builders.CharacterBuilder;

import java.sql.Connection;
import java.sql.SQLException;

public class CharacterDao {
    private final Executor executor;

    public CharacterDao(Connection connection) {
        this.executor = new Executor(connection);
    }

    public int add(Character character) throws SQLException {
        return executor.execPreparedUpdate("insert into characters(owner_id) values(?)",
                ps -> {
                    try {
                        ps.setInt(1, character.getOwnerId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }

    public Character get(int ownerId) throws SQLException {
        return executor.execPreparedQuery("select * from characters where owner_id = ?",
                result -> {
                    if (result.next()) {
                        return new CharacterBuilder()
                                .setId(result.getInt("id"))
                                .setMaxHealth(result.getInt("max_health"))
                                .setMaxDamage(result.getInt("max_damage"))
                                .setOwnerId(result.getInt("owner_id")).createCharacter();
                    }
                    return null;
                },
                ps -> {
                    try {
                        ps.setInt(1, ownerId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }
}
