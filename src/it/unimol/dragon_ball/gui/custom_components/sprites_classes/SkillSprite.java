package it.unimol.dragon_ball.gui.custom_components.sprites_classes;

import it.unimol.dragon_ball.app.logic.Character;
import it.unimol.dragon_ball.app.logic.Skill;
import it.unimol.dragon_ball.gui.custom_components.Sprite;
import it.unimol.dragon_ball.gui.custom_components.animations.SpriteAnimation;
import it.unimol.dragon_ball.utils.ResourcesHandler;
import it.unimol.dragon_ball.utils.Timer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Classe che rappresenta un'abilità del personaggio di gioco, disegnabile su pannello.
 * SkillSprite è un'estensione di {@link Sprite}. Pertanto, eredita le funzionalità descritte in quest'ultima classe.
 * La sua inizializzazione richiede un'istanza di {@link CharacterSprite} e una stringa che rappresenta la tipologia
 * dell'abilità, a scelta tra: "punch", "energyball", "special", "ultimate".
 */
public class SkillSprite extends Sprite {
    private String skillType;
    private CharacterSprite characterSprite;

    private String characterSkillsFilepath;

    private ArrayList<Image> punchAndKick;

    private ArrayList<Image> spritesEnergyball;
    private ArrayList<Image> energyballAnimation;

    private ArrayList<Image> spritesSpecialSkill;
    private Image tail;
    private Image body;
    private Point bodyPosition;
    private int bodyWidth;
    private Image head;
    private Point headPosition;

    private ArrayList<Image> spritesUltimateSkill;
    private ArrayList<Image> ultimateSkillAnimation;
    private final int ULTIMATE_SKILL_ANIMATION_FRAMES_NUMBER = 10;
    private Image ultimateSkillImage;

    private int spriteEnergyballNumber;
    private int spriteSpecialSkillNumber;

    private boolean isEnergyballCollided;
    private boolean isSpecialSkillCollided;
    private boolean isUltimateSkillCollided;

    private boolean isEnergyballCooldownEnded;
    private boolean isSpecialSkillCooldownEnded;
    private boolean isUltimateSkillCooldownEnded;

    private Rectangle skillCollisionRect;

    public SkillSprite(String skillType, CharacterSprite characterSprite) {
        super(characterSprite.getCharacter(), new Point(0, 0), characterSprite.getDimension(), characterSprite.getActualPanel(), characterSprite.isFlipped());

        this.setActiveSprite(null);

        this.skillType = skillType;
        this.characterSprite = characterSprite;
        initSkills();
        this.skillCollisionRect = new Rectangle();
    }

    private void initSkills() {
        Character character = this.getCharacter();
        String characterFilepath = "/resources/sprites/characters/" + character.getCanonicalName() + "/" + character.getActualEvolutionName() + "/";

        punchAndKick = new ArrayList<>();
        punchAndKick.add(ResourcesHandler.getInstance().getImage(characterFilepath + "PUNCH_0.png"));
        punchAndKick.add(ResourcesHandler.getInstance().getImage(characterFilepath + "PUNCH_1.png"));
        punchAndKick.add(ResourcesHandler.getInstance().getImage(characterFilepath + "PUNCH_0.png"));
        punchAndKick.add(ResourcesHandler.getInstance().getImage(characterFilepath + "PUNCH_1.png"));
        punchAndKick.add(ResourcesHandler.getInstance().getImage(characterFilepath + "KICK_0.png"));
        punchAndKick.add(ResourcesHandler.getInstance().getImage(characterFilepath + "KICK_1.png"));
        punchAndKick.add(ResourcesHandler.getInstance().getImage(characterFilepath + "KICK_2.png"));
        punchAndKick.add(ResourcesHandler.getInstance().getImage(characterFilepath + "KICK_1.png"));
        punchAndKick.add(ResourcesHandler.getInstance().getImage(characterFilepath + "KICK_2.png"));


        characterSkillsFilepath = "/resources/sprites/characters/" + character.getCanonicalName() + "/" + character.getActualEvolutionName() + "/skills/";

        spritesEnergyball = new ArrayList<>();
        spritesEnergyball.add(ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "ENERGYBALL_0.png"));
        spritesEnergyball.add(ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "ENERGYBALL_1.png"));
        spriteEnergyballNumber = 0;

        energyballAnimation = new ArrayList<>();
        energyballAnimation.add(ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "ENERGYBALL_TAIL_0.png"));
        energyballAnimation.add(ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "ENERGYBALL_TAIL_1.png"));
        energyballAnimation.add(ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "ENERGYBALL_TAIL_2.png"));
        energyballAnimation.add(ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "ENERGYBALL_TAIL_3.png"));
        energyballAnimation.add(ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "ENERGYBALL_TAIL_4.png"));

        isEnergyballCooldownEnded = true;
        isEnergyballCollided = false;


        spritesSpecialSkill = new ArrayList<>();
        if (getCharacter().getCanonicalName().equals("goku")) {
            spritesSpecialSkill.add(ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "KAMEHAMEHA_0.png"));
            spritesSpecialSkill.add(ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "KAMEHAMEHA_1.png"));
        } else if (getCharacter().getCanonicalName().equals("freezer")) {
            spritesSpecialSkill.add(ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "DEATH_BULLET_0.png"));
            spritesSpecialSkill.add(ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "DEATH_BULLET_1.png"));
        }
        isSpecialSkillCooldownEnded = true;
        isSpecialSkillCollided = false;
        bodyPosition = new Point();
        bodyWidth = this.getDimension().width;
        headPosition = new Point();


        isUltimateSkillCooldownEnded = true;
        isUltimateSkillCollided = false;
        spritesUltimateSkill = new ArrayList<>();
        ultimateSkillAnimation = new ArrayList<>(ULTIMATE_SKILL_ANIMATION_FRAMES_NUMBER);

        if (getCharacter().getCanonicalName().equals("goku")) {
            spritesUltimateSkill.add(ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "GENKIDAMA_0.png"));
            spritesUltimateSkill.add(ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "GENKIDAMA_1.png"));

            for (int i = 0; i < ULTIMATE_SKILL_ANIMATION_FRAMES_NUMBER; i++)
                ultimateSkillAnimation.add(ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "GENKIDAMA_ANIMATION_" + i + ".png"));

        } else if (getCharacter().getCanonicalName().equals("freezer")) {
            spritesUltimateSkill.add(ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "SUPERNOVA_0.png"));
            spritesUltimateSkill.add(ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "SUPERNOVA_1.png"));

            for (int i = 0; i < ULTIMATE_SKILL_ANIMATION_FRAMES_NUMBER; i++)
                ultimateSkillAnimation.add(ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "SUPERNOVA_ANIMATION_" + i + ".png"));
        }
    }


    private void showPunchAndKick() {
        SpriteAnimation punchAndKickAnimation = new SpriteAnimation(characterSprite, punchAndKick, getActualPanel(), characterSprite.getIsAnimationActive(), 180);
        punchAndKickAnimation.start();
    }

    private void showEnergyball() {
        /* Thread principale che consente di non bloccare il gioco*/
        Thread skillThread = new Thread(() -> {
            isEnergyballCooldownEnded = false;
            this.setActiveSprite(null);
            characterSprite.setEnergyballActive(true);

            /* Posizione iniziale della sfera di energia */
            if (this.isFlipped())
                getPosition().setLocation(characterSprite.getPosition().getX() - 30, characterSprite.getPosition().getY());
            else
                getPosition().setLocation(characterSprite.getPosition().getX() + 30, characterSprite.getPosition().getY());

            characterSprite.setActiveSprite(spritesEnergyball.get(spriteSpecialSkillNumber));
            if (spriteSpecialSkillNumber == 1) //Alterno le animazioni del CharacterSprite
                spriteSpecialSkillNumber = 0;
            else
                spriteSpecialSkillNumber++;

            try {
                Thread.sleep(100); // Tempo necessario a mostrare il player che lancia l'energyball
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            SpriteAnimation energyBallAnimation = new SpriteAnimation(this, energyballAnimation, getActualPanel(), characterSprite.getIsAnimationActive(), 100);
            energyBallAnimation.start();

            // Aspetto il tempo necessario da concludere l'animazione precedente per far muovere l'energyball
            try {
                Thread.sleep(100 * energyballAnimation.size());
                characterSprite.getIsAnimationActive().set(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            /* Lancio della sfera di energia */
            Thread energyballSphereMovement = new Thread(() -> {
                characterSkillsFilepath = "/resources/sprites/characters/" + getCharacter().getCanonicalName() + "/" + getCharacter().getActualEvolutionName() + "/skills/";
                head = ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "ENERGYBALL_SPHERE.png");
                this.setActiveSprite(head);
                isEnergyballCollided = false;

                if (this.isFlipped()) {
                    while (this.getPosition().getX() > 20 && !this.isEnergyballCollided) {
                        getPosition().setLocation(this.getPosition().getX() - 50, this.getPosition().getY());
                        getActualPanel().repaint();

                        try {
                            Thread.sleep(40);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    while (this.getPosition().getX() < 1200 && !this.isEnergyballCollided) {
                        getPosition().setLocation(this.getPosition().getX() + 50, this.getPosition().getY());
                        getActualPanel().repaint();

                        try {
                            Thread.sleep(40);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                characterSprite.setEnergyballActive(false);
                getActualPanel().repaint();
            });
            energyballSphereMovement.start();
            /* */


            double skillCooldown = getCharacter().getSkills().get(Skill.ENERGYBALL).getCooldown();
            Timer skillCooldownTimer = new Timer(skillCooldown, () -> this.isEnergyballCooldownEnded = true);
        });

        skillThread.start();
    }

    private void showSpecialSkill() {
        /* Thread principale che consente di non bloccare il gioco*/
        Thread skillThread = new Thread(() -> {
            resetSpecialSkillImages();
            resetSpecialSkillPosition();

            characterSprite.setSpecialSkillActive(true);

            isSpecialSkillCooldownEnded = false;
            this.setActiveSprite(null);

            /* Posizione iniziale della sfera di energia */
            if (this.isFlipped())
                this.getPosition().setLocation(characterSprite.getPosition().getX() - 60, characterSprite.getPosition().getY());
            else
                this.getPosition().setLocation(characterSprite.getPosition().getX() + 100, characterSprite.getPosition().getY());

            characterSprite.setActiveSprite(spritesSpecialSkill.get(0));
            getActualPanel().repaint();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            characterSprite.setActiveSprite(spritesSpecialSkill.get(1));
            getActualPanel().repaint();
            try {
                Thread.sleep(400); // Tempo necessario a mostrare il player che lancia l'energyball
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Thread specialSkillSphere = new Thread(() -> {
                setSpecialSkillImages();
                isSpecialSkillCollided = false;

                if (this.isFlipped()) {
                    bodyPosition.setLocation((int) this.getPosition().getX() - 100, (int) this.getPosition().getY());
                    headPosition.setLocation((int) this.getPosition().getX() - 70, (int) this.getPosition().getY());
                    bodyWidth = 0;
                    double headX = headPosition.getX();

                    while (headX > 20 && !isSpecialSkillCollided) {
                        bodyWidth = bodyWidth - 75;
                        headX = headX - 29;
                        this.bodyPosition.setLocation(this.bodyPosition.getX() + 24, this.bodyPosition.getY());
                        headPosition.setLocation((int) headX, (int) this.bodyPosition.getY());
                        getActualPanel().repaint();

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ignored) {

                        }
                    }

                } else {
                    bodyPosition.setLocation((int) this.getPosition().getX() + 45, (int) this.getPosition().getY());
                    headPosition.setLocation((int) this.getPosition().getX() + 45, (int) this.getPosition().getY());
                    bodyWidth = (int) this.getDimension().getWidth();
                    double headX = headPosition.getX();

                    while (headX < 1200 && !isSpecialSkillCollided) {
                        bodyWidth = bodyWidth + 75;
                        headX = headX + 31;
                        this.bodyPosition.setLocation(this.bodyPosition.getX() - 24, this.bodyPosition.getY());
                        headPosition.setLocation((int) headX, (int) this.bodyPosition.getY());
                        getActualPanel().repaint();

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
                characterSprite.setIsAnimationActive(false);
                characterSprite.setSpecialSkillActive(false);
                getActualPanel().repaint();
            });
            specialSkillSphere.start();

            double skillCooldown = getCharacter().getSkills().get(Skill.SPECIAL_SKILL).getCooldown();
            Timer skillCooldownTimer = new Timer(skillCooldown, () -> this.isSpecialSkillCooldownEnded = true);

        });
        skillThread.start();
    }

    private void resetSpecialSkillImages() {
        tail = null;
        body = null;
        head = null;
    }

    private void resetSpecialSkillPosition() {
        this.headPosition.setLocation(characterSprite.getPosition());
        this.getPosition().setLocation(characterSprite.getPosition());
    }


    private void setSpecialSkillImages() {
        characterSkillsFilepath = "/resources/sprites/characters/" + getCharacter().getCanonicalName() + "/" + getCharacter().getActualEvolutionName() + "/skills/";

        if (getCharacter().getCanonicalName().equals("goku")) {
            tail = ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "KAMEHAMEHA_TAIL.png");
            body = ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "KAMEHAMEHA_BODY.png");
            head = ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "KAMEHAMEHA_BALL.png");
        } else if (getCharacter().getCanonicalName().equals("freezer")) {
            tail = ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "DEATH_BULLET_TAIL.png");
            body = ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "DEATH_BULLET_BODY.png");
            head = ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "DEATH_BULLET_SPHERE.png");
        }
    }


    private void showUltimateSkill() {
        /* Thread principale che consente di non bloccare il gioco*/
        Thread skillThread = new Thread(() -> {
            characterSprite.setUltimateSkillActive(true);
            this.isUltimateSkillCooldownEnded = false;

            /* Posizione iniziale della sfera di energia */
            this.getPosition().setLocation(characterSprite.getPosition().getX(), characterSprite.getPosition().getY() - 100);

            Thread ultimateSkillAnimationThread = new Thread(() -> {
                isUltimateSkillCollided = true; // Evito che durante la crescita della sfera, il nemico subisca doppio danno
                int i = 0;
                while (i < 10) {
                    this.setActiveSprite(ultimateSkillAnimation.get(i));
                    i++;
                    getActualPanel().repaint();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            ultimateSkillAnimationThread.start();

            characterSprite.setActiveSprite(spritesUltimateSkill.get(0));
            getActualPanel().repaint();
            try {
                Thread.sleep(1100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            characterSprite.setActiveSprite(spritesUltimateSkill.get(1));
            getActualPanel().repaint();


            Thread ultimateSkillSphere = new Thread(() -> {
                setUltimateSkillImage();
                this.setActiveSprite(this.ultimateSkillImage);
                isUltimateSkillCollided = false;

                if (this.isFlipped()) {
                    while (this.getPosition().getY() < 530 && !isUltimateSkillCollided) {
                        this.getPosition().setLocation(this.getPosition().getX() - 75, this.getPosition().getY() + 50);
                        getActualPanel().repaint();

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ignored) {

                        }
                    }

                } else {
                    while (this.getPosition().getY() < 530 && !isUltimateSkillCollided) {
                        this.getPosition().setLocation(this.getPosition().getX() + 75, this.getPosition().getY() + 50);
                        getActualPanel().repaint();

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
                characterSprite.setIsAnimationActive(false);
                characterSprite.setUltimateSkillActive(false);
                getActualPanel().repaint();

            });
            ultimateSkillSphere.start();

            double skillCooldown = getCharacter().getSkills().get(Skill.ULTIMATE_SKILL).getCooldown();
            Timer skillCooldownTimer = new Timer(skillCooldown, () -> this.isUltimateSkillCooldownEnded = true);

        });
        skillThread.start();

    }

    private void setUltimateSkillImage() {
        if (this.getCharacter().getCanonicalName().equals("goku"))
            this.ultimateSkillImage = ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "GENKIDAMA_SPHERE.png");
        else if (this.getCharacter().getCanonicalName().equals("freezer"))
            this.ultimateSkillImage = ResourcesHandler.getInstance().getImage(characterSkillsFilepath + "SUPERNOVA_SPHERE.png");
    }


    public boolean isEnergyballCollided() {
        return isEnergyballCollided;
    }

    public void setEnergyballCollided(boolean energyballCollided) {
        isEnergyballCollided = energyballCollided;
    }

    public boolean isSpecialSkillCollided() {
        return isSpecialSkillCollided;
    }

    public void setSpecialSkillCollided(boolean specialSkillCollided) {
        isSpecialSkillCollided = specialSkillCollided;
    }

    public boolean isUltimateSkillCollided() {
        return isUltimateSkillCollided;
    }

    public void setUltimateSkillCollided(boolean ultimateSkillCollided) {
        isUltimateSkillCollided = ultimateSkillCollided;
    }

    /**
     * Mostra a video l'animazione della skill, a seconda della sua tipologia.
     */
    public void run() {
        assert skillType.equals("punch") || skillType.equals("energyball") || skillType.equals("special") || skillType.equals("ultimate") :
                "Skill type has to be 'punch', 'energyball', 'special' or 'ultimate'.";

        if (skillType.equals("punch"))
            showPunchAndKick();
        else if (skillType.equals("energyball") && isEnergyballCooldownEnded)
            showEnergyball();
        else if (skillType.equals("special") && isSpecialSkillCooldownEnded)
            showSpecialSkill();
        else if (skillType.equals("ultimate") && isUltimateSkillCooldownEnded)
            showUltimateSkill();
    }

    public Rectangle getSkillCollisionRect() {
        return skillCollisionRect;
    }

    private void handleCollisionRect() {
        Point position = this.getPosition();
        Dimension dimension = this.getDimension();

        if (characterSprite.isSpecialSkillActive()) {
            if (this.isFlipped())
                this.skillCollisionRect.x = (int) (this.headPosition.getX() + dimension.getWidth());
            else
                this.skillCollisionRect.x = (int) position.getX();

            this.skillCollisionRect.y = (int) position.getY();

            if (this.isFlipped())
                this.skillCollisionRect.width = (int) (characterSprite.getPosition().getX() - headPosition.getX());
            else
                this.skillCollisionRect.width = (int) ((headPosition.getX() + dimension.getWidth()) - position.getX());

            this.skillCollisionRect.height = (int) dimension.getHeight();

        } else if (characterSprite.isEnergyballActive() || characterSprite.isUltimateSkillActive()) {
            if (this.isFlipped())
                this.skillCollisionRect.x = (int) (position.getX() + dimension.getWidth());
            else
                this.skillCollisionRect.x = (int) position.getX();

            this.skillCollisionRect.y = (int) position.getY();

            if (this.isFlipped())
                this.skillCollisionRect.width = (int) -dimension.getWidth();
            else
                this.skillCollisionRect.width = (int) dimension.getWidth();

            this.skillCollisionRect.height = (int) dimension.getHeight();

            if (characterSprite.isEnergyballActive())
                this.skillCollisionRect.grow(-40, -40); // In order to best fit with the image
        }

    }

    public boolean isEnergyballCooldownEnded() {
        return isEnergyballCooldownEnded;
    }

    public boolean isSpecialSkillCooldownEnded() {
        return isSpecialSkillCooldownEnded;
    }

    public boolean isUltimateSkillCooldownEnded() {
        return isUltimateSkillCooldownEnded;
    }

    @Override
    public void draw(Graphics g) {
        handleCollisionRect();
        // COLLISION RECTANGLE
        // g.drawRect(skillCollisionRect.x, skillCollisionRect.y, skillCollisionRect.width, skillCollisionRect.height);

        int x = (int) this.getPosition().getX();
        int y = (int) this.getPosition().getY();
        int width = this.getDimension().width;
        int height = this.getDimension().height;

        if (characterSprite.isSpecialSkillActive()) {
            g.drawImage(body, (int) bodyPosition.getX(), (int) bodyPosition.getY(), bodyWidth, height, null);
            g.drawImage(tail, x - 20, y, width, height, null);
            g.drawImage(head, (int) headPosition.getX(), (int) headPosition.getY(), width, height, null);

        } else
            g.drawImage(this.getActiveSprite(), x, y, width, height, null);

    }
}
