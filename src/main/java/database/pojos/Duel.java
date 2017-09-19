package database.pojos;

import java.util.List;

public class Duel {
    private long duelID;
    private long user1ID;
    private long user2ID;
    private List<String> log;

    public Duel(long duelID, long user1ID, long user2ID, List<String> log) {
        this.duelID = duelID;
        this.user1ID = user1ID;
        this.user2ID = user2ID;
        this.log = log;
    }

    public Duel() {
    }

    public long getDuelID() {
        return duelID;
    }

    public void setDuelID(long duelID) {
        this.duelID = duelID;
    }

    public long getUser1ID() {
        return user1ID;
    }

    public void setUser1ID(long user1ID) {
        this.user1ID = user1ID;
    }

    public long getUser2ID() {
        return user2ID;
    }

    public void setUser2ID(long user2ID) {
        this.user2ID = user2ID;
    }

    public List<String> getLog() {
        return log;
    }

    public void setLog(List<String> log) {
        this.log = log;
    }
}
