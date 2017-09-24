package pojos;

public class User {
    private int id;
    private String username;
    private String password;
    private int rating;
    private boolean ready;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.rating = 0;
    }

    public User(int id, String username, String password, int rating, boolean ready) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rating = rating;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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
                ", rating=" + rating +
                ", ready=" + ready +
                '}';
    }
}
