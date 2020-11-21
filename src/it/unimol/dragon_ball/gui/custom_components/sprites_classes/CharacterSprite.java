package it.unimol.dragon_ball.gui.custom_components.sprites_classes;

import it.unimol.dragon_ball.app.logic.Character;
import it.unimol.dragon_ball.app.logic.Skill;
import it.unimol.dragon_ball.gui.custom_components.Sprite;
import it.unimol.dragon_ball.gui.custom_components.animations.SpriteAnimation;
import it.unimol.dragon_ball.utils.ResourcesHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Classe che rappresenta un personaggio del gioco disegnabile su pannello.
 * CharacterSprite è un'estensione di {@link Sprite}. Pertanto, eredita il costruttore e le funzionalità descritte
 * in quest'ultima classe.
 */
public class CharacterSprite extends Sprite {
    private Image idle;
    private Image move_back;
    private Image move_forward;

    private ArrayList<SkillSprite> skillSprites;
    private boolean isAuraActive;
    private boolean isPunchActive;
    private boolean isEnergyballActive;
    private boolean isSpecialSkillActive;
    private boolean isUltimateSkillActive;

    private ArrayList<Image> auraCharge;
    private ArrayList<Image> damageSprites;
    private int evolutionNumber;

    private Rectangle characterCollisionRect;
    private boolean isCharacterSpriteInitReady;


    public CharacterSprite(Character character, Point position, Dimension dimension, JPanel actualPanel, boolean isFlipped) {
        super(character, position, dimension, actualPanel, isFlipped);

        initCharacterSprite();
        while (!isCharacterSpriteInitReady) { // Tempo necessario ad inizializzare il personaggio
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }

        this.setActiveSprite(this.idle);
        this.evolutionNumber = getCharacter().getActualEvolutionNumber();
    }

    private void initCharacterSprite() {
        // Creo un thread perchè l'inizializzazione del personaggio è un procedimento molto pesante dal punto di vista dell'elaborazione
        Thread initCharacterSpriteThread = new Thread(() -> {
            isCharacterSpriteInitReady = false;

            Character character = this.getCharacter();
            String characterFilepath = "/resources/sprites/characters/" + character.getCanonicalName() + "/" + character.getActualEvolutionName() + "/";

            idle = ResourcesHandler.getInstance().getImage(characterFilepath + "IDLE.png");
            move_back = ResourcesHandler.getInstance().getImage(characterFilepath + "MOVE_BACK.png");
            move_forward = ResourcesHandler.getInstance().getImage(characterFilepath + "MOVE_FORWARD.png");

            isAuraActive = false;
            isPunchActive = false;
            isEnergyballActive = false;
            isSpecialSkillActive = false;
            isUltimateSkillActive = false;

            auraCharge = new ArrayList<>();
            auraCharge.add(ResourcesHandler.getInstance().getImage(characterFilepath + "AURA_0.png"));
            auraCharge.add(ResourcesHandler.getInstance().getImage(characterFilepath + "AURA_1.png"));
            auraCharge.add(ResourcesHandler.getInstance().getImage(characterFilepath + "AURA_2.png"));
            auraCharge.add(ResourcesHandler.getInstance().getImage(characterFilepath + "AURA_1.png"));
            auraCharge.add(ResourcesHandler.getInstance().getImage(characterFilepath + "AURA_2.png"));
            auraCharge.add(ResourcesHandler.getInstance().getImage(characterFilepath + "AURA_1.png"));
            auraCharge.add(ResourcesHandler.getInstance().getImage(characterFilepath + "AURA_2.png"));
            auraCharge.add(ResourcesHandler.getInstance().getImage(characterFilepath + "AURA_0.png"));

            damageSprites = new ArrayList<>();
            damageSprites.add(ResourcesHandler.getInstance().getImage(characterFilepath + "DAMAGE_0.png"));
            damageSprites.add(ResourcesHandler.getInstance().getImage(characterFilepath + "DAMAGE_1.png"));
            damageSprites.add(ResourcesHandler.getInstance().getImage(characterFilepath + "DAMAGE_2.png"));

            skillSprites = new ArrayList<>(4);
            skillSprites.add(new SkillSprite("punch", this));
            skillSprites.add(new SkillSprite("energyball", this));
            skillSprites.add(new SkillSprite("special", this));
            skillSprites.add(new SkillSprite("ultimate", this));

            this.characterCollisionRect = new Rectangle();

            isCharacterSpriteInitReady = true;
        });

        initCharacterSpriteThread.start();
    }

    public Image getIdle() {
        return idle;
    }

    /**
     * Sposta il disegno di {@link CharacterSprite} sul pannello secondo la direzione presa in input.
     * Qualora le coordinate dello {@link Sprite} dovessero superare i confini del pannello giocabile, tale metodo non
     * avrà effetto.
     * @param direction direzione del movimento del personaggio. E' possibile scegliere tra le seguenti parole chiave:
     *                  "up", "right", "left", "down".
     */
    public void move(String direction) {
        assert direction.equals("up") || direction.equals("right") || direction.equals("left") || direction.equals("down");

        Point newPosition = new Point(this.getPosition().x, this.getPosition().y);

        if (direction.equals("up")) {
            newPosition.y = this.getPosition().y - (this.getCharacter().getSpeed() - 2);
            this.setActiveSprite(move_forward);
        }

        if (direction.equals("right")) {
            newPosition.x = this.getPosition().x + (this.getCharacter().getSpeed() - 2);

            if (this.isFlipped())
                this.setActiveSprite(move_back);
            else
                this.setActiveSprite(move_forward);
        }

        if (direction.equals("left")) {
            newPosition.x = this.getPosition().x - (this.getCharacter().getSpeed() - 2);

            if (this.isFlipped())
                this.setActiveSprite(move_forward);
            else
                this.setActiveSprite(move_back);
        }

        if (direction.equals("down")) {
            newPosition.y = this.getPosition().y + (this.getCharacter().getSpeed() - 2);
            this.setActiveSprite(move_back);
        }

        if ((newPosition.y >= 150 && newPosition.y <= 600) && ((newPosition.x >= 100 && newPosition.x <= 1160))) {
            this.getPosition().setLocation(newPosition);
            this.getActualPanel().repaint();
        }
    }


    public ArrayList<SkillSprite> getSkillSprites() {
        return skillSprites;
    }

    public void showAuraCharge() {
        SpriteAnimation auraAnimation = new SpriteAnimation(this, auraCharge, getActualPanel(), this.getIsAnimationActive(), 0);
        auraAnimation.start();
    }

    public void showDamageByPunches(AtomicBoolean isEnemyPunchActive) {
        SpriteAnimation damageAnimation = new SpriteAnimation(this, damageSprites, getActualPanel(), isEnemyPunchActive, 200);
        damageAnimation.start();
    }

    public void showDamageBySkill() {
        this.setActiveSprite(damageSprites.get(0));
    }


    public boolean isAuraActive() {
        return isAuraActive;
    }

    public void setAuraActive(boolean auraActive) {
        isAuraActive = auraActive;
    }

    public boolean isPunchActive() {
        return isPunchActive;
    }

    public void setPunchActive(boolean punchActive) {
        isPunchActive = punchActive;
    }

    public void setEnergyballActive(boolean energyballActive) {
        isEnergyballActive = energyballActive;
    }

    public boolean isEnergyballActive() {
        return isEnergyballActive;
    }

    public void setSpecialSkillActive(boolean specialSkillActive) {
        isSpecialSkillActive = specialSkillActive;
    }

    public boolean isSpecialSkillActive() {
        return isSpecialSkillActive;
    }

    public boolean isUltimateSkillActive() {
        return isUltimateSkillActive;
    }

    public void setUltimateSkillActive(boolean ultimateSkillActive) {
        isUltimateSkillActive = ultimateSkillActive;
    }

    public boolean isSkillActive() {
        return this.isPunchActive() || this.isEnergyballActive() || this.isSpecialSkillActive() || this.isUltimateSkillActive();
    }

    public Rectangle getCharacterCollisionRect() {
        return characterCollisionRect;
    }

    private void handleCollisionRect() {
        Point position = this.getPosition();
        Dimension dimension = this.getDimension();

        if (this.isFlipped())
            this.characterCollisionRect.x = (int) (position.getX() + dimension.getWidth());
        else
            this.characterCollisionRect.x = (int) position.getX();

        this.characterCollisionRect.y = (int) position.getY();

        if (this.isFlipped())
            this.characterCollisionRect.width = (int) -dimension.getWidth();
        else
            this.characterCollisionRect.width = (int) dimension.getWidth();

        this.characterCollisionRect.height = (int) dimension.getHeight();

        this.characterCollisionRect.grow(-20, -20);  // In order to best fit with the image
    }

    @Override
    public void draw(Graphics g) {
        handleCollisionRect();
        // COLLISION RECTANGLE
        // g.drawRect(characterCollisionRect.x, characterCollisionRect.y, characterCollisionRect.width, characterCollisionRect.height);

        // Per aggiornare lo sprite in caso di evoluzione
        if (evolutionNumber < getCharacter().getActualEvolutionNumber()) {
            this.setIsAnimationActive(false);
            evolutionNumber++;
            initCharacterSprite();
            this.setIsAnimationActive(false);
        }

        int width = this.getDimension().width;
        int height = this.getDimension().height;
        int x = (int) this.getPosition().getX();
        int y = (int) this.getPosition().getY();

        g.drawImage(this.getActiveSprite(), x, y, width, height, null);

        /* SKILLS HANDLING */
        if (this.isEnergyballActive) {
            this.skillSprites.get(Skill.ENERGYBALL).draw(g);
        } else if (this.isSpecialSkillActive) {
            this.skillSprites.get(Skill.SPECIAL_SKILL).draw(g);
        } else if (this.isUltimateSkillActive) {
            this.skillSprites.get(Skill.ULTIMATE_SKILL).draw(g);
        }
        /**/
    }

}
