package database.pojos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Duel {
    private int duelId;
    private final Map<String, User> sessionToUser = new HashMap<>();
    private final Map<String, Character> sessionToCharacter = new HashMap<>();
    private User user1;
    private User user2;
    private Character character1;
    private Character character2;
    private List<String> log;

    public Duel() {
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public Character getCharacter1() {
        return character1;
    }

    public void setCharacter1(Character character1) {
        this.character1 = character1;
    }

    public Character getCharacter2() {
        return character2;
    }

    public void setCharacter2(Character character2) {
        this.character2 = character2;
    }

    public int getDuelId() {
        return duelId;
    }

    public void setDuelId(int duelId) {
        this.duelId = duelId;
    }

    public List<String> getLog() {
        return log;
    }

    public void setLog(List<String> log) {
        this.log = log;
    }

    @Override
    public String toString() {
        return "Duel{" +
                "duelId=" + duelId +
                ", user1=" + user1.getUsername() +
                ", user2=" + user2.getUsername() +
                ", character1=" + character1.getOwnerId() +
                ", character2=" + character2.getOwnerId() +
                '}';
    }
}
