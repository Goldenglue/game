package pojos;

import managers.DuelManager;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Duel {
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, Character> characters = new HashMap<>();
    private final Map<Integer, List<String>> logs = new HashMap<>();
    private final DuelManager duelManager;
    private AtomicBoolean ready = new AtomicBoolean(false);
    private Instant startMoment;


    public Duel() {
        this.duelManager = new DuelManager(this);
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

    public void addLog(int userId, List<String> log) {
        logs.put(userId, log);
    }

    public Map<Integer, List<String>> getLogs() {
        return logs;
    }

    public void setReady() {
        ready.set(true);
    }

    public boolean getStatus() {
        return ready.get();
    }

    public void start() {
        startMoment = Instant.now();
    }

    public long secondsAfterStart() {
        return Duration.between(startMoment, Instant.now()).getSeconds();
    }

    public DuelManager getDuelManager() {
        return duelManager;
    }

    @Override
    public String toString() {
        return "Duel{" +
                "users=" + users +
                ", characters=" + characters +
                ", logs=" + logs +
                ", ready=" + ready +
                '}';
    }


}
