package database.services;

import database.dao.CharacterDao;
import database.pojos.Character;
import database.pojos.User;

import java.sql.SQLException;

public class CharactersService implements Service {

    public int addNewCharacter(User user) throws SQLException {
        CharacterDao dao = new CharacterDao(source.getConnection());
        Character character = new Character(user.getId());
        return dao.add(character);
    }

}
