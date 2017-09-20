package database.pojos;

public class User {
    private int id;
    private String username;
    private String password;
    private int reputation;
    private boolean ready;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.reputation = 0;
    }

    public User(int id, String username, String password, int reputation, boolean ready) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.reputation = reputation;
        this.ready = ready;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", reputation=" + reputation +
                ", ready=" + ready +
                '}';
    }
}
