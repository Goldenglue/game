package database.services;

import pojos.Character;
import pojos.User;

public interface CharacterService {
    int addNewCharacter(User user);

    Character get(int id);

    int updateAfterMatch(int ownerId);

}
