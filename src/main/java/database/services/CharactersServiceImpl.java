package database.services;

import database.DataSource;
import database.dao.CharacterDao;
import pojos.Character;
import pojos.User;

public class CharactersServiceImpl implements CharacterService {
    private final DataSource source;

    public CharactersServiceImpl(DataSource source) {
        this.source = source;
    }

    @Override
    public int addNewCharacter(User user) {
        CharacterDao dao = new CharacterDao(source.getConnection());
        Character character = new Character(user.getId());
        return dao.add(character);
    }

    @Override
    public Character get(int id) {
        CharacterDao dao = new CharacterDao(source.getConnection());
        return dao.get(id);
    }

    @Override
    public int updateAfterMatch(int ownerId) {
        CharacterDao dao = new CharacterDao(source.getConnection());
        return dao.updateStats(ownerId);
    }
}
