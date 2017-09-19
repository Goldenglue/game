package database.pojos;

public class User {
    private long ID;
    private String username;
    private String password;
    private int reputation;
    private long characterID;
    private boolean ready;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(long ID, String username, String password, int reputation, long characterID, boolean ready) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.reputation = reputation;
        this.characterID = characterID;
        this.ready = ready;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCharacterID() {
        return characterID;
    }

    public void setCharacterID(long characterID) {
        this.characterID = characterID;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", reputation=" + reputation +
                ", characterID=" + characterID +
                ", ready=" + ready +
                '}';
    }
}
