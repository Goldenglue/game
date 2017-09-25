package database.services;

import database.dao.CharacterDao;
import pojos.Character;
import pojos.User;

public class CharactersService implements Service {

    public int addNewCharacter(User user) {
        CharacterDao dao = new CharacterDao(source.getConnection());
        Character character = new Character(user.getId());
        return dao.add(character);
    }

    public Character get(int id) {
        CharacterDao dao = new CharacterDao(source.getConnection());
        return dao.get(id);
    }

    public int updateAfterMatch(int ownerId) {
        CharacterDao dao = new CharacterDao(source.getConnection());
        return dao.updateStats(ownerId);
    }
}
