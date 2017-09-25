package pojos.builders;

import pojos.Character;

public class CharacterBuilder {
    private int ownerId;
    private int id;
    private int maxHealth;
    private int maxDamage;

    public CharacterBuilder setOwnerId(int ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public CharacterBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public CharacterBuilder setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        return this;
    }

    public CharacterBuilder setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
        return this;
    }

    public Character createCharacter() {
        return new Character(id, maxHealth, maxDamage, ownerId);
    }
}