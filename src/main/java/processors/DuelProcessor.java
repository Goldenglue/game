package processors;

import pojos.Character;
import pojos.Duel;
import pojos.User;

import java.util.List;
import java.util.Map;

public class DuelProcessor {
    private final Map<Integer, User> users;
    private final Map<Integer, Character> characters;
    private final Map<Integer, List<String>> logs;

    public DuelProcessor(Duel duel) {
        this.users = duel.getUsers();
        this.characters = duel.getCharacters();
        this.logs = duel.getLogs();
    }

    public synchronized boolean process(int userId, int opponentId) {
        return checkFightState(userId, opponentId) && dealDmg(userId, opponentId);
    }

    private boolean dealDmg(int userId, int opponentId) {
        User user = users.get(userId);
        User opponent = users.get(opponentId);
        Character userCharacter = characters.get(userId);
        Character opponentCharacter = characters.get(opponentId);

        opponentCharacter.reduceHealth(userCharacter.getCurrentDamage());
        writeLog("Вы ударили " + opponent.getUsername() + " на " + userCharacter.getCurrentDamage() + "урона", userId);
        writeLog(user.getUsername() + " ударил вас на " + userCharacter.getCurrentDamage() + "урона", opponentId);
        if (checkFightState(userId, opponentId)) {
            return true;
        } else {
            writeLog("Вы убили " + opponent.getUsername(), userId);
            writeLog("Вас убил " + user.getUsername(), opponentId);
            return false;
        }
    }

    private boolean checkFightState(int userId, int opponentId) {
        return characters.get(userId).getCurrentHealth() >= 0 && characters.get(opponentId).getCurrentHealth() >= 0;
    }

    private void writeLog(String line, int id) {
        List<String> list = logs.get(id);
        list.add(line);
    }
}
