package managers;

import pojos.Character;
import pojos.Duel;
import pojos.User;

import java.util.List;
import java.util.Map;

public class DuelManager {
    private final Map<Integer, User> users;
    private final Map<Integer, Character> characters;
    private final Map<Integer, List<String>> logs;
    private int winnerId;

    public DuelManager(Duel duel) {
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
        writeLog("Вы ударили " + opponent.getUsername() + " на " + userCharacter.getCurrentDamage() + " урона", userId);
        writeLog(user.getUsername() + " ударил вас на " + userCharacter.getCurrentDamage() + " урона", opponentId);
        if (checkFightState(userId, opponentId)) {
            return true;
        } else {
            setResult(userId);
            writeLog("Вы убили " + opponent.getUsername(), userId);
            writeLog("Вас убил " + user.getUsername(), opponentId);
            return false;
        }
    }

    private boolean checkFightState(int userId, int opponentId) {
        return characters.get(userId).getCurrentHealth() > 0 && characters.get(opponentId).getCurrentHealth() > 0;
    }

    private void writeLog(String line, int id) {
        logs.get(id).add(line);
    }

    private void setResult(int winnerId) {
        this.winnerId = winnerId;
    }

    public boolean didIWin(int userId) {
        return winnerId == userId;
    }
}
