package it.unimol.dragon_ball.app.logic;

/**
 * Classe che descrive un'abilità e i suoi attributi.
 * Quando viene inizializzata, viene aggiornato il danno massimo che può procurare.
 */
public class Skill {
    private String name;
    private Character character;
    private int skillDamage;
    private int damageDealt;
    private int mana;
    private double cooldown; // Expressed in seconds

    public static final int PUNCH = 0;
    public static final int ENERGYBALL = 1;
    public static final int SPECIAL_SKILL = 2;
    public static final int ULTIMATE_SKILL = 3;

    public Skill(String name, Character character, int skillDamage, int mana, double cooldown) {
        this.name = name;
        this.character = character;
        this.skillDamage = skillDamage;
        this.mana = mana;
        this.cooldown = cooldown;

        this.damageDealt = this.skillDamage + character.getAtk();
    }

    public String getName() {
        return name;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public int getSkillDamage() {
        return skillDamage;
    }

    public int getDamageDealt() {
        return damageDealt;
    }

    public int getMana() {
        return mana;
    }

    /**
     * Aggiorna il danno massimo che l'abilità può procurare, aggiungendo al danno della skill l'attacco del personaggio
     * che possiede tale abilità.
     */
    public void updateDamageDealt() {
        this.damageDealt = this.skillDamage + character.getAtk();
    }

    public double getCooldown() {
        return cooldown;
    }
}
