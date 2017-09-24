package pojos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Duel {
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, Character> characters = new HashMap<>();
    private final Map<Integer, List<String>> logs = new HashMap<>();
    private boolean ready = false;

    public Duel() {
    }

    public void addUser(int userId, User user) {
        users.put(userId, user);
    }

    public void addCharacter(int userId, Character character) {
        characters.put(userId, character);
    }

    public List<String> getLog(int userId) {
        return logs.get(userId);
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    public Map<Integer, Character> getCharacters() {
        return characters;
    }

    public Map<Integer, List<String>> getLogs() {
        return logs;
    }

    public void changeStatus() {
        ready = !ready;
    }

    public boolean getStatus() {
        return ready;
    }
}
