package database.pojos;

public class Character {
    private long ID;
    private short maxHealth;
    private short maxDamage;
    private short currentHealth;
    private short currentDamage;
    private long ownerID;

    public Character() {
    }

    public Character(long ID, short maxHealth, short maxDamage, short currentHealth, short currentDamage, long ownerID) {
        this.ID = ID;
        this.maxHealth = maxHealth;
        this.maxDamage = maxDamage;
        this.currentHealth = currentHealth;
        this.currentDamage = currentDamage;
        this.ownerID = ownerID;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public short getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(short maxHealth) {
        this.maxHealth = maxHealth;
    }

    public short getMaxDamage() {
        return maxDamage;
    }

    public void setMaxDamage(short maxDamage) {
        this.maxDamage = maxDamage;
    }

    public short getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(short currentHealth) {
        this.currentHealth = currentHealth;
    }

    public short getCurrentDamage() {
        return currentDamage;
    }

    public void setCurrentDamage(short currentDamage) {
        this.currentDamage = currentDamage;
    }

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }
}
