package database.pojos;

public class User {
    private long ID;
    private String username;
    private String password;
    private int reputation;
    private long characterID;

    public User() {
    }

    public User(long ID, String username, String password, int reputation, long characterID) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.reputation = reputation;
        this.characterID = characterID;
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
}
