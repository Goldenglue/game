package database.services;

import database.dao.CharacterDao;
import pojos.Character;
import pojos.User;

import java.sql.SQLException;

public class CharactersService implements Service {

    public int addNewCharacter(User user) throws SQLException {
        CharacterDao dao = new CharacterDao(source.getConnection());
        Character character = new Character(user.getId());
        return dao.add(character);
    }

    public Character get(int id) throws SQLException {
        CharacterDao dao = new CharacterDao(source.getConnection());
        return dao.get(id);
    }
}
