package database.pojos;

import java.util.List;

public class Duel {
    private int duelId;
    private int user1Id;
    private int character1Id;
    private int user2Id;
    private int character2Id;
    private List<String> log;

    public Duel(int duelId, int user1Id, int character1Id, int user2Id, int character2Id, List<String> log) {
        this.duelId = duelId;
        this.user1Id = user1Id;
        this.character1Id = character1Id;
        this.user2Id = user2Id;
        this.character2Id = character2Id;
        this.log = log;
    }

    public Duel() {
    }

    public int getDuelId() {
        return duelId;
    }

    public void setDuelId(int duelId) {
        this.duelId = duelId;
    }

    public int getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    public List<String> getLog() {
        return log;
    }

    public void setLog(List<String> log) {
        this.log = log;
    }

    public int getCharacter1Id() {
        return character1Id;
    }

    public void setCharacter1Id(int character1Id) {
        this.character1Id = character1Id;
    }

    public int getCharacter2Id() {
        return character2Id;
    }

    public void setCharacter2Id(int character2Id) {
        this.character2Id = character2Id;
    }
}
