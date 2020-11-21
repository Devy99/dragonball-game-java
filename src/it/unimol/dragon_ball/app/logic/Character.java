package it.unimol.dragon_ball.app.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * Classe che descrive le caratteristiche fondamentali di un personaggio giocante.
 * L'inizializzazione richiede il passaggio di un file properties come parametro, file contenete i dati da fornire
 * agli attributi della classe stessa.
 */
public class Character {
    private String canonicalName;
    private String[] evolutionsNames;
    private String actualEvolutionName;

    private final int MAX_HP;
    private int actualMaxHp;
    private int actualHP;
    private int hpIncreaseRate;

    private final int MAX_MP;
    private int actualMaxMp;
    private int actualMP;
    private int mpIncreaseRate;

    private int atk;
    private int atkIncreaseRate;

    private int speed;

    private int maxAura;
    private int aura;
    private final int MAX_EVOLUTIONS_NUMBER;
    private int actualEvolutionNumber;

    private List<Skill> skills;


    /**
     * Il costruttore preleva i dati dal file properties e li inserisce direttamente agli attributi della classe.
     * Alcuni dati vengono inizializzati a partire da quelli ricavati dal file properties.
     * @param characterProperties file properties che presenta i dati da fornire al personaggio.
     */
    public Character(Properties characterProperties) {
        assert characterProperties != null;
        this.canonicalName = characterProperties.getProperty("canonical_name");
        this.MAX_EVOLUTIONS_NUMBER = Integer.parseInt(characterProperties.getProperty("max_evolution_number"));

        evolutionsNames = new String[this.MAX_EVOLUTIONS_NUMBER + 1]; // Includo anche la forma iniziale che non rappresenta una evoluzione
        for (int i = 0; i < this.MAX_EVOLUTIONS_NUMBER + 1; i++)
            evolutionsNames[i] = characterProperties.getProperty("evolution" + i + "Name");

        this.actualEvolutionName = evolutionsNames[0];

        this.actualHP = Integer.parseInt(characterProperties.getProperty("hp"));
        this.hpIncreaseRate = Integer.parseInt(characterProperties.getProperty("hpIncreaseRate"));
        this.actualMP = 0;
        this.mpIncreaseRate = Integer.parseInt(characterProperties.getProperty("mpIncreaseRate"));
        this.atk = Integer.parseInt(characterProperties.getProperty("atk"));
        this.atkIncreaseRate = Integer.parseInt(characterProperties.getProperty("atkIncreaseRate"));
        this.speed = Integer.parseInt(characterProperties.getProperty("speed"));

        this.MAX_HP = this.actualHP + (this.hpIncreaseRate * this.MAX_EVOLUTIONS_NUMBER);
        this.actualMaxHp = this.actualHP;
        this.MAX_MP = this.actualMP + (this.mpIncreaseRate * this.MAX_EVOLUTIONS_NUMBER);
        this.actualMaxMp = Integer.parseInt(characterProperties.getProperty("mp"));
        this.actualEvolutionNumber = 0;
        this.aura = 1;
        this.maxAura = 100;

        skills = new ArrayList<>();
    }


    public String getCanonicalName() {
        return canonicalName;
    }


    /**
     * Restituisce il nome della trasformazione di questo personaggio attraverso un indice numerico.
     * Si asserisce che l'indice inserito sia corente a quelli disponibili nella lista correlata.
     * @param index indice che fa riferimento alla lista dei nomi delle trasformazioni del personaggio.
     * @return <code>String</code> che fa riferimento al nome della trasformazione del personaggio.
     */
    public String getEvolutionNameByIndex(int index) {
        assert (index > 0) && (index <= this.MAX_EVOLUTIONS_NUMBER);

        return evolutionsNames[index];
    }

    public int getMAX_HP() {
        return MAX_HP;
    }

    public int getHpIncreaseRate() {
        return hpIncreaseRate;
    }

    public int getMAX_MP() {
        return MAX_MP;
    }

    public int getMpIncreaseRate() {
        return mpIncreaseRate;
    }

    public int getAtkIncreaseRate() {
        return atkIncreaseRate;
    }

    public int getSpeed() {
        return speed;
    }

    public int getMAX_EVOLUTIONS_NUMBER() {
        return MAX_EVOLUTIONS_NUMBER;
    }

    public String getActualEvolutionName() {
        return actualEvolutionName;
    }

    public void setActualEvolutionName(String actualEvolutionName) {
        this.actualEvolutionName = actualEvolutionName;
    }

    public int getActualMaxHp() {
        return actualMaxHp;
    }

    public int getActualMaxMp() {
        return actualMaxMp;
    }

    public int getActualHP() {
        return actualHP;
    }

    public void setActualHP(int actualHP) {
        this.actualHP = actualHP;
    }

    public int getActualMP() {
        return actualMP;
    }


    /**
     * Reimposta il valore degli MP attuali del personaggio fino al valore massimo raggiungibile (<code>actualMaxMp</code>).
     * Qualora venga passato un numero negativo come parametro, lancia un NotEnoughManaException.
     * @param actualMP Mp attualmente a disposizione del personaggio.
     * @throws NotEnoughManaException quando il valore passato come input è un intero negativo.
     */
    public void setActualMP(int actualMP) throws NotEnoughManaException {
        if (actualMP < 0)
            throw new NotEnoughManaException("Insufficient Mana");

        if (actualMP < this.actualMaxMp)
            this.actualMP = actualMP;
    }

    public int getAtk() {
        return atk;
    }


    public int getMaxAura() {
        return maxAura;
    }

    public int getAura() {
        return aura;
    }

    /**
     * Reimposta il valore dei punti aura attuali del personaggio fino al valore massimo raggiungibile (<code>maxAura</code>).
     * Qualora il personaggio avesse raggiunto la massima evoluzione, l'aura non viene aggiornata.
     * @param aura punti aura attualmente a disposizione del personaggio.
     */
    public void setAura(int aura) {
        if (this.getActualEvolutionNumber() < this.getMAX_EVOLUTIONS_NUMBER() && aura <= maxAura)
            this.aura = aura;
    }

    /**
     * Resetta i punti aura del personaggio ad 1.
     */
    public void resetAura() {
        this.aura = 1;
    }

    public int getActualEvolutionNumber() {
        return actualEvolutionNumber;
    }

    public String[] getEvolutionsNames() {
        return evolutionsNames;
    }

    /**
     * Aggiorna le statistiche del personaggio e il danno delle sue skill a quelle della sua trasformazione successiva.
     * Si asserisce che tale aggiornamento avviene soltanto qualora non si abbia ancora raggiunto la massima trasformazione.
     */
    public void evolve() {
        assert actualEvolutionNumber < this.MAX_EVOLUTIONS_NUMBER;

        increaseActualEvolutionNumber();
        updateStats();

        for (Skill skill : skills)
            skill.updateDamageDealt();

    }

    private void increaseActualEvolutionNumber() {
        this.actualEvolutionNumber = actualEvolutionNumber + 1;
    }


    private void updateStats() {
        this.actualEvolutionName = getEvolutionNameByIndex(this.actualEvolutionNumber);

        this.actualMaxHp = this.actualMaxHp + hpIncreaseRate;
        this.actualHP = this.actualHP + hpIncreaseRate;

        this.actualMaxMp = this.actualMaxMp + mpIncreaseRate;
        this.actualMP = this.actualMP + mpIncreaseRate;

        this.atk = this.atk + atkIncreaseRate;

        this.maxAura = this.maxAura + 40;
    }

    public void addNewSkill(Skill newSkill) {
        assert newSkill != null;

        this.skills.add(newSkill);
    }

    public List<Skill> getSkills() {
        return this.skills;
    }

}
