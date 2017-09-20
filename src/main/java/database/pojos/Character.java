package database.pojos;

public class Character {
    private int id;
    private int maxHealth;
    private int maxDamage;
    private int currentHealth;
    private int currentDamage;
    private int ownerId;

    public Character() {
    }

    public Character(int ownerId) {
        this.ownerId = ownerId;
    }

    public Character(int id, int maxHealth, int maxDamage, int ownerId) {
        this.id = id;
        this.maxHealth = maxHealth;
        this.maxDamage = maxDamage;
        this.currentHealth = maxHealth;
        this.currentDamage = maxDamage;
        this.ownerId = ownerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getCurrentDamage() {
        return currentDamage;
    }

    public void setCurrentDamage(int currentDamage) {
        this.currentDamage = currentDamage;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
