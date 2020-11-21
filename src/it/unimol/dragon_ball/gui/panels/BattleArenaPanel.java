package it.unimol.dragon_ball.gui.panels;

import it.unimol.dragon_ball.app.GameController;
import it.unimol.dragon_ball.app.logic.NotEnoughManaException;
import it.unimol.dragon_ball.gui.GuiHandler;
import it.unimol.dragon_ball.gui.custom_components.bars.StatusBar;
import it.unimol.dragon_ball.gui.custom_components.sprites_classes.CharacterSprite;
import it.unimol.dragon_ball.gui.frames.MainFrame;
import it.unimol.dragon_ball.utils.ResourceException;
import it.unimol.dragon_ball.utils.ResourcesHandler;
import it.unimol.dragon_ball.app.logic.Character;
import it.unimol.dragon_ball.app.logic.Skill;
import it.unimol.dragon_ball.utils.SoundException;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Classe che rappresenta il pannello relativo al combattimento tra i personaggi selezionati.
 * BattleArenaPanel è un'estensione di JPanel.
 * Quando viene istanziato, si ha l'inizializzazione del pannello e della musica di background.
 * L'inizializzazione del pannello comprende:
 *      - l'inizializzazione dei flag associati a ciascun giocatore;
 *      - l'inizializzazione del KeyListener per l'interazione con i personaggi del gioco;
 *      - l'inizializzazione del PlayersActionThread che gestisce le azioni dei personaggi in relazione agli input
 *        ricevuti dalla tastiera;
 *      - l'inizializzazione del Thread che gestisce le collisioni del tipo player-player, player-skill, skill-skill.
 */
public class BattleArenaPanel extends JPanel {

    private final Dimension PANEL_SIZE = MainFrame.FRAME_SIZE;

    private Clip backgroundMusicClip;
    private Image backgroundImage;

    private Character player1;
    private Character player2;

    private StatusBar player1StatusBar;
    private StatusBar player2StatusBar;

    private CharacterSprite player1Sprite;
    private CharacterSprite player2Sprite;

    private Image escPressLogo;

    private Image player1WinnerBar;
    private Image player1Logo;
    private Image player2WinnerBar;
    private Image player2Logo;
    private Image winnerLogo;

    private boolean isFirstErrorSound;
    private boolean isEventTriggered;
    private boolean isGameFinished;

    private String winnerName;

    /* PLAYERS FLAG INDEX */
    private final int UP = 0;
    private final int LEFT = 1;
    private final int DOWN = 2;
    private final int RIGHT = 3;
    private final int AURA = 4;
    private final int PUNCH = 5;
    private final int ENERGYBALL = 6;
    private final int SPECIAL_SKILL = 7;
    private final int ULTIMATE_SKILL = 8;
    private final int TOTAL_FLAG = 9;
    int i = 0;
    /* */

    /*   PLAYER1 FLAGS    */
    private ArrayList<AtomicBoolean> player1Flags;
    /**/

    /*   PLAYER2 FLAGS    */
    private ArrayList<AtomicBoolean> player2Flags;
    /**/

    public BattleArenaPanel() {
        initPanel();
        initBackgroundMusic();
    }

    private void initBackgroundMusic() {
        try {
            backgroundMusicClip = ResourcesHandler.getInstance().playMusic("/resources/sounds/other/BattleBackgroundSound.wav", Clip.LOOP_CONTINUOUSLY);
        } catch (ResourceException ignored) {
        }

        Thread charactersVoiceThread = new Thread(() -> {
            /* PLAYER1 CHARACTER START VOICE */
            try {
                ResourcesHandler.getInstance().playMusic("/resources/sounds/battle_sounds/" + player1.getCanonicalName() + "StartGame.wav", 0);
            } catch (ResourceException ignored) {
            }

            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /* PLAYER2 CHARACTER START VOICE */
            try {
                ResourcesHandler.getInstance().playMusic("/resources/sounds/battle_sounds/" + player2.getCanonicalName() + "StartGame.wav", 0);
            } catch (ResourceException ignored) {
            }
        });
        charactersVoiceThread.start();
    }

    private void stopBackgroundMusic() {
        assert this.backgroundMusicClip != null;
        try {
            ResourcesHandler.getInstance().stopMusic(backgroundMusicClip);
        } catch (SoundException ignored) {

        }
    }

    private void initPanel() {
        String backgroundMapFilepath = GuiHandler.getInstance().getSelectedBackgroundMapFilepath();
        backgroundImage = ResourcesHandler.getInstance().getImage(backgroundMapFilepath);

        player1 = GameController.getInstance().getPlayer1();
        player2 = GameController.getInstance().getPlayer2();

        player1StatusBar = new StatusBar(player1, new Point(20, 20), false);
        player2StatusBar = new StatusBar(player2, new Point(1147, 20), true);

        player1Sprite = new CharacterSprite(player1, new Point(120, 400), new Dimension(100, 100), this, false);
        player2Sprite = new CharacterSprite(player2, new Point(1160, 400), new Dimension(-100, 100), this, true);

        escPressLogo = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/EscToContinue.png");

        player1WinnerBar = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/Player1WinnerBar.png");
        player1Logo = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/Player1Logo.png");
        player2WinnerBar = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/Player2WinnerBar.png");
        player2Logo = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/Player2Logo.png");
        winnerLogo = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/WinnerLogo.png");

        isFirstErrorSound = true;
        isEventTriggered = false;
        isGameFinished = false;

        initPlayersFlags();
        initListener();
        initPlayersActionThread();
        initCollisionChecker();

        this.setSize(PANEL_SIZE);
        this.setLayout(null);
    }


    private void initPlayersFlags() {
        player1Flags = new ArrayList<>(this.TOTAL_FLAG);
        player2Flags = new ArrayList<>(this.TOTAL_FLAG);

        for (int i = 0; i < this.TOTAL_FLAG; i++) {
            player1Flags.add(new AtomicBoolean(false));
            player2Flags.add(new AtomicBoolean(false));
        }
    }

    private void initListener() {
        MainFrame.getInstance().setFocusable(true);
        MainFrame.getInstance().requestFocus();

        // I listener settano soltanto dei flag. Non cambiano direttamente lo stato degli sprite
        MainFrame.getInstance().addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) player1Flags.get(LEFT).set(true);
                if (e.getKeyCode() == KeyEvent.VK_D) player1Flags.get(RIGHT).set(true);
                if (e.getKeyCode() == KeyEvent.VK_W) player1Flags.get(UP).set(true);
                if (e.getKeyCode() == KeyEvent.VK_S) player1Flags.get(DOWN).set(true);

                if (e.getKeyCode() == KeyEvent.VK_SPACE) player1Flags.get(AURA).set(true);
                if (e.getKeyCode() == KeyEvent.VK_Q) player1Flags.get(PUNCH).set(true);
                if (e.getKeyCode() == KeyEvent.VK_E) player1Flags.get(ENERGYBALL).set(true);
                if (e.getKeyCode() == KeyEvent.VK_R) player1Flags.get(SPECIAL_SKILL).set(true);
                if (e.getKeyCode() == KeyEvent.VK_T) player1Flags.get(ULTIMATE_SKILL).set(true);


                if (e.getKeyCode() == KeyEvent.VK_LEFT) player2Flags.get(LEFT).set(true);
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) player2Flags.get(RIGHT).set(true);
                if (e.getKeyCode() == KeyEvent.VK_UP) player2Flags.get(UP).set(true);
                if (e.getKeyCode() == KeyEvent.VK_DOWN) player2Flags.get(DOWN).set(true);

                if (e.getKeyCode() == KeyEvent.VK_ENTER) player2Flags.get(AURA).set(true);
                if (e.getKeyCode() == KeyEvent.VK_U) player2Flags.get(PUNCH).set(true);
                if (e.getKeyCode() == KeyEvent.VK_I) player2Flags.get(ENERGYBALL).set(true);
                if (e.getKeyCode() == KeyEvent.VK_O) player2Flags.get(SPECIAL_SKILL).set(true);
                if (e.getKeyCode() == KeyEvent.VK_P) player2Flags.get(ULTIMATE_SKILL).set(true);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) player1Flags.get(LEFT).set(false);
                if (e.getKeyCode() == KeyEvent.VK_D) player1Flags.get(RIGHT).set(false);
                if (e.getKeyCode() == KeyEvent.VK_W) player1Flags.get(UP).set(false);
                if (e.getKeyCode() == KeyEvent.VK_S) player1Flags.get(DOWN).set(false);


                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    player1Sprite.setAuraActive(false);
                    player1Sprite.getIsAnimationActive().set(false);
                    player1Flags.get(AURA).set(false);
                }
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    player1Sprite.setPunchActive(false);
                    player1Sprite.getIsAnimationActive().set(false);
                    player1Flags.get(PUNCH).set(false);
                }

                if (e.getKeyCode() == KeyEvent.VK_E) {
                    player1Flags.get(ENERGYBALL).set(false);
                }

                if (e.getKeyCode() == KeyEvent.VK_R) {
                    player1Flags.get(SPECIAL_SKILL).set(false);
                }

                if (e.getKeyCode() == KeyEvent.VK_T) {
                    player1Flags.get(ULTIMATE_SKILL).set(false);
                }


                if (e.getKeyCode() == KeyEvent.VK_LEFT) player2Flags.get(LEFT).set(false);
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) player2Flags.get(RIGHT).set(false);
                if (e.getKeyCode() == KeyEvent.VK_UP) player2Flags.get(UP).set(false);
                if (e.getKeyCode() == KeyEvent.VK_DOWN) player2Flags.get(DOWN).set(false);


                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    player2Sprite.setAuraActive(false);
                    player2Sprite.getIsAnimationActive().set(false);
                    player2Flags.get(AURA).set(false);
                }

                if (e.getKeyCode() == KeyEvent.VK_U) {
                    player2Sprite.setPunchActive(false);
                    player2Sprite.getIsAnimationActive().set(false);
                    player2Flags.get(PUNCH).set(false);
                }

                if (e.getKeyCode() == KeyEvent.VK_I) {
                    player2Flags.get(ENERGYBALL).set(false);
                }

                if (e.getKeyCode() == KeyEvent.VK_O) {
                    player2Flags.get(SPECIAL_SKILL).set(false);
                }

                if (e.getKeyCode() == KeyEvent.VK_P) {
                    player2Flags.get(ULTIMATE_SKILL).set(false);
                }

                if (!player1Sprite.isSkillActive())
                    player1Sprite.setActiveSprite(player1Sprite.getIdle());

                if (!player2Sprite.isSkillActive())
                    player2Sprite.setActiveSprite(player2Sprite.getIdle());

                if (!GameController.getInstance().isGameActive() && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    stopBackgroundMusic();
                    MainFrame.getInstance().switchPanel(MainFrame.MAIN_SCREEN_PANEL);
                }

            }

        });
    }


    private void initPlayersActionThread() {
        Thread playerActionThread = new Thread(() -> {

            while (GameController.getInstance().isGameActive()) {
                handlePlayersActions(player1Sprite, player1, player1Flags);
                handlePlayersActions(player2Sprite, player2, player2Flags);

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.isGameFinished = true;
            this.winnerName = GameController.getInstance().getWinnerName();
            this.repaint();

            Thread winnerSoundThread = new Thread(() -> {
                try {
                    ResourcesHandler.getInstance().playMusic("/resources/sounds/battle_sounds/WinnerSound.wav", 0);
                } catch (ResourceException ignored) {
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }

                Character winnerCharacter = null;
                if (winnerName.equals("player1"))
                    winnerCharacter = player1;
                else if (winnerName.equals("player2"))
                    winnerCharacter = player2;

                try {
                    ResourcesHandler.getInstance().playMusic("/resources/sounds/battle_sounds/" + winnerCharacter.getCanonicalName() + "WinnerSound.wav", 0);
                } catch (ResourceException ignored) {
                }
            });
            winnerSoundThread.start();

        });
        playerActionThread.start();
    }

    private void handlePlayersActions(CharacterSprite playerSprite, Character player, ArrayList<AtomicBoolean> booleans) {
        if (!playerSprite.isSkillActive()) {
            if (booleans.get(this.LEFT).get() && booleans.get(this.UP).get() && !playerSprite.isAuraActive()) {
                playerSprite.move("left");
                playerSprite.move("up");

            } else if (booleans.get(this.LEFT).get() && booleans.get(this.DOWN).get() && !playerSprite.isAuraActive()) {
                playerSprite.move("left");
                playerSprite.move("down");

            } else if (booleans.get(this.RIGHT).get() && booleans.get(this.UP).get() && !playerSprite.isAuraActive()) {
                playerSprite.move("right");
                playerSprite.move("up");

            } else if (booleans.get(this.RIGHT).get() && booleans.get(this.DOWN).get() && !playerSprite.isAuraActive()) {
                playerSprite.move("right");
                playerSprite.move("down");

            } else if (booleans.get(this.LEFT).get() && !playerSprite.isAuraActive()) {
                playerSprite.move("left");

            } else if (booleans.get(this.UP).get() && !playerSprite.isAuraActive()) {
                playerSprite.move("up");

            } else if (booleans.get(this.RIGHT).get() && !playerSprite.isAuraActive()) {
                playerSprite.move("right");

            } else if (booleans.get(this.DOWN).get() && !playerSprite.isAuraActive()) {
                playerSprite.move("down");

            } else if (booleans.get(this.AURA).get()) {
                if (!playerSprite.getIsAnimationActive().get() && !playerSprite.isAuraActive()) {
                    playerSprite.setIsAnimationActive(true);
                    playerSprite.setAuraActive(true);
                    playerSprite.showAuraCharge();
                    try {
                        ResourcesHandler.getInstance().playAnimationMusic("/resources/sounds/battle_sounds/Aura.wav", 0, playerSprite.getIsAnimationActive());
                    } catch (ResourceException ignored) {
                    }
                }

                player.setAura(player.getAura() + 1);
                try {
                    player.setActualMP(player.getActualMP() + 4);
                } catch (NotEnoughManaException ignored) {
                    //MP only increase
                }

            } else if (booleans.get(PUNCH).get() && playerSprite.getSkillSprites().size() >= 1) {
                if (!playerSprite.getIsAnimationActive().get()) {
                    playerSprite.setIsAnimationActive(true);
                    playerSprite.getSkillSprites().get(Skill.PUNCH).run();
                }

                try {
                    ResourcesHandler.getInstance().playMusic("/resources/sounds/battle_sounds/VoidPunches.wav", 0);
                } catch (ResourceException ignored) {
                }

                playerSprite.setPunchActive(true);

                try {
                    Thread.sleep(80);  // Evito che i danni da pugno siano eccessivi
                } catch (InterruptedException ignored) {
                }

            } else if (booleans.get(ENERGYBALL).get()) {
                try {
                    if (playerSprite.getSkillSprites().size() >= 2 &&
                            playerSprite.getSkillSprites().get(Skill.ENERGYBALL).isEnergyballCooldownEnded()) {
                        player.setActualMP(player.getActualMP() - player.getSkills().get(Skill.ENERGYBALL).getMana());
                        playerSprite.setIsAnimationActive(true);
                        playerSprite.getSkillSprites().get(Skill.ENERGYBALL).run();
                        handleEnergyballSound(player);
                    }
                } catch (NotEnoughManaException e) {
                    if (isFirstErrorSound) {
                        try {
                            ResourcesHandler.getInstance().playMusic("/resources/sounds/sounds_effects/ManaError.wav", 0);
                            isFirstErrorSound = false;
                            Thread resetFirstErrorSound = new Thread(() -> {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException ignored) {
                                }
                                this.isFirstErrorSound = true;
                            });
                            resetFirstErrorSound.start();
                        } catch (ResourceException ignored) {
                        }
                    }
                }

            } else if (booleans.get(SPECIAL_SKILL).get()) {
                try {
                    if (playerSprite.getSkillSprites().size() >= 3 &&
                            playerSprite.getSkillSprites().get(Skill.SPECIAL_SKILL).isSpecialSkillCooldownEnded()) {
                        player.setActualMP(player.getActualMP() - player.getSkills().get(Skill.SPECIAL_SKILL).getMana());
                        playerSprite.setIsAnimationActive(true);
                        playerSprite.getSkillSprites().get(Skill.SPECIAL_SKILL).run();
                        handleSpecialSkillSound(player);
                    }

                } catch (NotEnoughManaException e) {
                    if (isFirstErrorSound) {
                        try {
                            ResourcesHandler.getInstance().playMusic("/resources/sounds/sounds_effects/ManaError.wav", 0);
                            isFirstErrorSound = false;
                            Thread resetFirstErrorSound = new Thread(() -> {
                                try {
                                    Thread.sleep(998);
                                } catch (InterruptedException ignored) {
                                }
                                this.isFirstErrorSound = true;
                            });
                            resetFirstErrorSound.start();
                        } catch (ResourceException ignored) {
                        }
                    }
                }

            } else if (booleans.get(ULTIMATE_SKILL).get()) {
                try {
                    if (playerSprite.getSkillSprites().size() >= 4 &&
                            playerSprite.getSkillSprites().get(Skill.ULTIMATE_SKILL).isUltimateSkillCooldownEnded()) {
                        player.setActualMP(player.getActualMP() - player.getSkills().get(Skill.ULTIMATE_SKILL).getMana());
                        playerSprite.setIsAnimationActive(true);
                        playerSprite.getSkillSprites().get(Skill.ULTIMATE_SKILL).run();
                        handleUltimateSkillSound(player);
                    }

                } catch (NotEnoughManaException e) {
                    if (isFirstErrorSound) {
                        try {
                            ResourcesHandler.getInstance().playMusic("/resources/sounds/sounds_effects/ManaError.wav", 0);
                            isFirstErrorSound = false;
                            Thread resetFirstErrorSound = new Thread(() -> {
                                try {
                                    Thread.sleep(999);
                                } catch (InterruptedException ignored) {
                                }
                                this.isFirstErrorSound = true;
                            });
                            resetFirstErrorSound.start();
                        } catch (ResourceException ignored) {
                        }
                    }
                }
            }
        }
    }

    private void handleEnergyballSound(Character character) {
        if (character.getCanonicalName().equals("goku")) {
            try {
                ResourcesHandler.getInstance().playMusic("/resources/sounds/battle_sounds/GokuEnergyball.wav", 0);
            } catch (ResourceException ignored) {
            }
        } else if (character.getCanonicalName().equals("freezer")) {
            try {
                ResourcesHandler.getInstance().playMusic("/resources/sounds/battle_sounds/FreezerEnergyball.wav", 0);
            } catch (ResourceException ignored) {
            }
        }
    }

    private void handleSpecialSkillSound(Character character) {
        if (character.getCanonicalName().equals("goku")) {

            Thread musicThread = new Thread(() -> {
                try {
                    ResourcesHandler.getInstance().playMusic("/resources/sounds/battle_sounds/Kamehameha0.wav", 0);
                    Thread.sleep(1000);
                    ResourcesHandler.getInstance().playMusic("/resources/sounds/battle_sounds/Kamehameha1.wav", 0);
                } catch (ResourceException | InterruptedException ignored) {
                }
            });
            musicThread.start();

        } else if (character.getCanonicalName().equals("freezer")) {
            Thread musicThread = new Thread(() -> {
                try {
                    ResourcesHandler.getInstance().playMusic("/resources/sounds/battle_sounds/DeathBullet0.wav", 0);
                    Thread.sleep(1000);
                    ResourcesHandler.getInstance().playMusic("/resources/sounds/battle_sounds/DeathBullet1.wav", 0);
                } catch (ResourceException | InterruptedException ignored) {
                }
            });
            musicThread.start();
        }
    }

    private void handleUltimateSkillSound(Character character) {
        if (character.getCanonicalName().equals("goku")) {

            Thread musicThread = new Thread(() -> {
                try {
                    ResourcesHandler.getInstance().playMusic("/resources/sounds/battle_sounds/Genkidama0.wav", 0);
                    Thread.sleep(1000);
                    ResourcesHandler.getInstance().playMusic("/resources/sounds/battle_sounds/Genkidama1.wav", 0);
                } catch (ResourceException | InterruptedException ignored) {
                }
            });
            musicThread.start();

        } else if (character.getCanonicalName().equals("freezer")) {
            Thread musicThread = new Thread(() -> {
                try {
                    ResourcesHandler.getInstance().playMusic("/resources/sounds/battle_sounds/Supernova0.wav", 0);
                } catch (ResourceException ignored) {
                }
            });
            musicThread.start();
        }
    }


    private void initCollisionChecker() {
        Thread collisionsThread = new Thread(() -> {

            AtomicBoolean arePlayersColliding = new AtomicBoolean(false);
            AtomicBoolean player1PunchDamageAnimation = new AtomicBoolean(false);
            AtomicBoolean player2PunchDamageAnimation = new AtomicBoolean(false);

            while (GameController.getInstance().isGameActive()) {
                arePlayersColliding.set(player1Sprite.getCharacterCollisionRect().intersects(player2Sprite.getCharacterCollisionRect()));
                player2PunchDamageAnimation.set(arePlayersColliding.get() && player1Sprite.getIsAnimationActive().get() && player1Sprite.isPunchActive());
                player1PunchDamageAnimation.set(arePlayersColliding.get() && player2Sprite.getIsAnimationActive().get() && player2Sprite.isPunchActive());


                /* HANDLING COLLISION WITH PLAYER2 */
                if (player1Sprite.getSkillSprites().size() >= 4) {
                    boolean hasPlayer1EnergyballHitPlayer2 = player1Sprite.getSkillSprites().get(Skill.ENERGYBALL).getSkillCollisionRect().intersects(player2Sprite.getCharacterCollisionRect());
                    boolean isPlayer1EnergyballNotCollided = !player1Sprite.getSkillSprites().get(Skill.ENERGYBALL).isEnergyballCollided();
                    boolean hasPlayer1SpecialSkillHitPlayer2 = player1Sprite.getSkillSprites().get(Skill.SPECIAL_SKILL).getSkillCollisionRect().intersects(player2Sprite.getCharacterCollisionRect());
                    boolean isPlayer1SpecialSkillNotCollided = !player1Sprite.getSkillSprites().get(Skill.SPECIAL_SKILL).isSpecialSkillCollided();
                    boolean hasPlayer1UltimateSkillHitPlayer2 = player1Sprite.getSkillSprites().get(Skill.ULTIMATE_SKILL).getSkillCollisionRect().intersects(player2Sprite.getCharacterCollisionRect());
                    boolean isPlayer1UltimateSkillNotCollided = !player1Sprite.getSkillSprites().get(Skill.ULTIMATE_SKILL).isUltimateSkillCollided();

                    if (arePlayersColliding.get() && player1Sprite.isPunchActive()) {
                        try {
                            ResourcesHandler.getInstance().playMusic("/resources/sounds/battle_sounds/VoidPunches.wav", 0);
                        } catch (ResourceException ignored) {
                        }

                        player2.setActualHP(player2.getActualHP() - player1.getSkills().get(Skill.PUNCH).getDamageDealt());
                        player2Sprite.showDamageByPunches(player2PunchDamageAnimation);
                        try {
                            Thread.sleep(80);
                        } catch (InterruptedException ignored) {

                        }

                    } else if (hasPlayer1EnergyballHitPlayer2 && player1Sprite.isEnergyballActive() && isPlayer1EnergyballNotCollided) {
                        startCollisionSound();
                        player1Sprite.getSkillSprites().get(Skill.ENERGYBALL).setEnergyballCollided(true);
                        player2.setActualHP(player2.getActualHP() - player1.getSkills().get(Skill.ENERGYBALL).getDamageDealt());
                        player2Sprite.showDamageBySkill();

                    } else if (hasPlayer1SpecialSkillHitPlayer2 && player1Sprite.isSpecialSkillActive() && isPlayer1SpecialSkillNotCollided) {
                        startCollisionSound();
                        player1Sprite.getSkillSprites().get(Skill.SPECIAL_SKILL).setSpecialSkillCollided(true);
                        player2.setActualHP(player2.getActualHP() - player1.getSkills().get(Skill.SPECIAL_SKILL).getDamageDealt());
                        player2Sprite.showDamageBySkill();

                    } else if (hasPlayer1UltimateSkillHitPlayer2 && player1Sprite.isUltimateSkillActive() && isPlayer1UltimateSkillNotCollided) {
                        startCollisionSound();
                        player1Sprite.getSkillSprites().get(Skill.ULTIMATE_SKILL).setUltimateSkillCollided(true);
                        player2.setActualHP(player2.getActualHP() - player1.getSkills().get(Skill.ULTIMATE_SKILL).getDamageDealt());
                        player2Sprite.showDamageBySkill();
                    }
                }

                /* HANDLING COLLISION WITH PLAYER1 */
                if (player2Sprite.getSkillSprites().size() >= 4) {
                    boolean hasPlayer2EnergyballHitPlayer1 = player2Sprite.getSkillSprites().get(Skill.ENERGYBALL).getSkillCollisionRect().intersects(player1Sprite.getCharacterCollisionRect());
                    boolean isPlayer2EnergyballNotCollided = !player2Sprite.getSkillSprites().get(Skill.ENERGYBALL).isEnergyballCollided();
                    boolean hasPlayer2UltimateSkillHitPlayer1 = player2Sprite.getSkillSprites().get(Skill.ULTIMATE_SKILL).getSkillCollisionRect().intersects(player1Sprite.getCharacterCollisionRect());
                    boolean isPlayer2UltimateSkillNotCollided = !player2Sprite.getSkillSprites().get(Skill.ULTIMATE_SKILL).isUltimateSkillCollided();
                    boolean hasPlayer2SpecialSkillHitPlayer1 = player2Sprite.getSkillSprites().get(Skill.SPECIAL_SKILL).getSkillCollisionRect().intersects(player1Sprite.getCharacterCollisionRect());
                    boolean isPlayer2SpecialSkillNotCollided = !player2Sprite.getSkillSprites().get(Skill.SPECIAL_SKILL).isSpecialSkillCollided();


                    if (arePlayersColliding.get() && player2Sprite.isPunchActive()) {
                        player1.setActualHP(player1.getActualHP() - player2.getSkills().get(Skill.PUNCH).getDamageDealt());
                        player1Sprite.showDamageByPunches(player1PunchDamageAnimation);

                        try {
                            ResourcesHandler.getInstance().playMusic("/resources/sounds/battle_sounds/VoidPunches.wav", 0);
                        } catch (ResourceException ignored) {
                        }

                        try {
                            Thread.sleep(80);
                        } catch (InterruptedException ignored) {
                        }

                    } else if (hasPlayer2EnergyballHitPlayer1 && player2Sprite.isEnergyballActive() && isPlayer2EnergyballNotCollided) {
                        player2Sprite.getSkillSprites().get(Skill.ENERGYBALL).setEnergyballCollided(true);
                        player1.setActualHP(player1.getActualHP() - player2.getSkills().get(Skill.ENERGYBALL).getDamageDealt());
                        player1Sprite.showDamageBySkill();
                        startCollisionSound();

                    } else if (hasPlayer2SpecialSkillHitPlayer1 && player2Sprite.isSpecialSkillActive() && isPlayer2SpecialSkillNotCollided) {
                        player2Sprite.getSkillSprites().get(Skill.SPECIAL_SKILL).setSpecialSkillCollided(true);
                        player1.setActualHP(player1.getActualHP() - player2.getSkills().get(Skill.SPECIAL_SKILL).getDamageDealt());
                        player1Sprite.showDamageBySkill();
                        startCollisionSound();

                    } else if (hasPlayer2UltimateSkillHitPlayer1 && player2Sprite.isUltimateSkillActive() && isPlayer2UltimateSkillNotCollided) {
                        player1.setActualHP(player1.getActualHP() - player2.getSkills().get(Skill.ULTIMATE_SKILL).getDamageDealt());
                        player1Sprite.showDamageBySkill();
                        player2Sprite.getSkillSprites().get(Skill.ULTIMATE_SKILL).setUltimateSkillCollided(true);
                        startCollisionSound();
                    }

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                /* HANDLING SKILL COLLISION WITH OTHER SKILLS */
                if (player1Sprite.getSkillSprites().size() >= 4 && player2Sprite.getSkillSprites().size() >= 4 &&
                        player1Sprite.isSkillActive() && player2Sprite.isSkillActive()) {

                    // Amplifico la lunghezza del rettangolo della energyball per rendere possibile l'intersezione tra i due rettangoli
                    Rectangle player1EnergyballCollisionRectangleAmplified = player1Sprite.getSkillSprites().get(Skill.ENERGYBALL).getSkillCollisionRect();
                    player1EnergyballCollisionRectangleAmplified.grow(10, 0);
                    Rectangle player2EnergyballCollisionRectangleAmplified = player2Sprite.getSkillSprites().get(Skill.ENERGYBALL).getSkillCollisionRect();
                    player2EnergyballCollisionRectangleAmplified.grow(10, 0);
                    boolean hasPlayer1EnergyballHitPlayer2Energyball = player1EnergyballCollisionRectangleAmplified.intersects(player2EnergyballCollisionRectangleAmplified);
                    boolean hasPlayer1EnergyballHitPlayer2SpecialSkill = player1Sprite.getSkillSprites().get(Skill.ENERGYBALL).getSkillCollisionRect().intersects(player2Sprite.getSkillSprites().get(Skill.SPECIAL_SKILL).getSkillCollisionRect());
                    boolean hasPlayer1EnergyballHitPlayer2UltimateSkill = player1Sprite.getSkillSprites().get(Skill.ENERGYBALL).getSkillCollisionRect().intersects(player2Sprite.getSkillSprites().get(Skill.ULTIMATE_SKILL).getSkillCollisionRect());
                    boolean hasPlayer2EnergyballHitPlayer1UltimateSkill = player2Sprite.getSkillSprites().get(Skill.ENERGYBALL).getSkillCollisionRect().intersects(player1Sprite.getSkillSprites().get(Skill.ULTIMATE_SKILL).getSkillCollisionRect());
                    boolean hasPlayer2EnergyballHitPlayer1SpecialSkill = player2Sprite.getSkillSprites().get(Skill.ENERGYBALL).getSkillCollisionRect().intersects(player1Sprite.getSkillSprites().get(Skill.SPECIAL_SKILL).getSkillCollisionRect());

                    boolean hasPlayer1SpecialSkillHitPlayer2SpecialSkill = player1Sprite.getSkillSprites().get(Skill.SPECIAL_SKILL).getSkillCollisionRect().intersects(player2Sprite.getSkillSprites().get(Skill.SPECIAL_SKILL).getSkillCollisionRect());
                    boolean hasPlayer1UltimateSkillHitPlayer2UltimateSkill = player1Sprite.getSkillSprites().get(Skill.ULTIMATE_SKILL).getSkillCollisionRect().intersects(player2Sprite.getSkillSprites().get(Skill.ULTIMATE_SKILL).getSkillCollisionRect());

                    boolean isPlayer1EnergyballNotCollided = !player1Sprite.getSkillSprites().get(Skill.ENERGYBALL).isEnergyballCollided();
                    boolean isPlayer2EnergyballNotCollided = !player2Sprite.getSkillSprites().get(Skill.ENERGYBALL).isEnergyballCollided();
                    boolean isPlayer1SpecialSkillNotCollided = !player1Sprite.getSkillSprites().get(Skill.SPECIAL_SKILL).isSpecialSkillCollided();
                    boolean isPlayer2SpecialSkillNotCollided = !player2Sprite.getSkillSprites().get(Skill.SPECIAL_SKILL).isSpecialSkillCollided();
                    boolean isPlayer1UltimateSkillNotCollided = !player1Sprite.getSkillSprites().get(Skill.ULTIMATE_SKILL).isUltimateSkillCollided();
                    boolean isPlayer2UltimateSkillNotCollided = !player2Sprite.getSkillSprites().get(Skill.ULTIMATE_SKILL).isUltimateSkillCollided();


                    if (hasPlayer1EnergyballHitPlayer2Energyball && isPlayer1EnergyballNotCollided && isPlayer2EnergyballNotCollided) {
                        player1Sprite.getSkillSprites().get(Skill.ENERGYBALL).setEnergyballCollided(true);
                        player2Sprite.getSkillSprites().get(Skill.ENERGYBALL).setEnergyballCollided(true);
                        startCollisionSound();
                    } else if (((player2Sprite.isSpecialSkillActive() && hasPlayer1EnergyballHitPlayer2SpecialSkill) || (player2Sprite.isUltimateSkillActive() && hasPlayer1EnergyballHitPlayer2UltimateSkill)) && isPlayer1EnergyballNotCollided) {
                        player1Sprite.getSkillSprites().get(Skill.ENERGYBALL).setEnergyballCollided(true);
                        startCollisionSound();
                    } else if (((player1Sprite.isSpecialSkillActive() && hasPlayer2EnergyballHitPlayer1SpecialSkill) || (player1Sprite.isUltimateSkillActive() && hasPlayer2EnergyballHitPlayer1UltimateSkill)) && isPlayer2EnergyballNotCollided) {
                        player2Sprite.getSkillSprites().get(Skill.ENERGYBALL).setEnergyballCollided(true);
                        startCollisionSound();
                    } else if (hasPlayer1SpecialSkillHitPlayer2SpecialSkill && isPlayer1SpecialSkillNotCollided && isPlayer2SpecialSkillNotCollided) {
                        player1Sprite.getSkillSprites().get(Skill.SPECIAL_SKILL).setSpecialSkillCollided(true);
                        player2Sprite.getSkillSprites().get(Skill.SPECIAL_SKILL).setSpecialSkillCollided(true);
                        startCollisionSound();
                    } else if (hasPlayer1UltimateSkillHitPlayer2UltimateSkill && isPlayer1UltimateSkillNotCollided && isPlayer2UltimateSkillNotCollided) {
                        player1Sprite.getSkillSprites().get(Skill.ULTIMATE_SKILL).setUltimateSkillCollided(true);
                        player2Sprite.getSkillSprites().get(Skill.ULTIMATE_SKILL).setUltimateSkillCollided(true);
                        startCollisionSound();
                    }

                }

            }
        });
        collisionsThread.start();
    }

    private void startCollisionSound() {
        try {
            ResourcesHandler.getInstance().playMusic("/resources/sounds/battle_sounds/Collision.wav", 0);
        } catch (ResourceException ignored) {
        }
    }


    private void setEventTriggered(boolean eventTriggered) {
        isEventTriggered = eventTriggered;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, PANEL_SIZE.width, PANEL_SIZE.height, this);

        player1StatusBar.draw(g);
        player2StatusBar.draw(g);

        player1Sprite.draw(g);
        player2Sprite.draw(g);

        if (isGameFinished) {
            g.drawImage(escPressLogo, 250, 530, 787, 91, this);

            g.drawImage(player1WinnerBar, 20, 300, 550, 288, this);
            g.drawImage(player1Logo, 200, 350, 289, 81, this);
            g.drawImage(player2WinnerBar, 710, 300, 550, 288, this);
            g.drawImage(player2Logo, 770, 350, 289, 81, this);

            if (winnerName.equals("player1"))
                g.drawImage(winnerLogo, 300, 450, 200, 50, this);
            else if (winnerName.equals("player2"))
                g.drawImage(winnerLogo, 800, 450, 200, 50, this);

            if (player1.getCanonicalName().equals("goku"))
                g.drawImage(ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/Goku" + player1.getActualEvolutionNumber() + ".png"), -150, 250, 500, 380, this);
            else if (player1.getCanonicalName().equals("freezer"))
                g.drawImage(ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/Freezer" + player1.getActualEvolutionNumber() + ".png"), -150, 250, 500, 380, this);

            if (player2.getCanonicalName().equals("goku"))
                g.drawImage(ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/Goku" + player2.getActualEvolutionNumber() + ".png"), 900, 250, 500, 380, this);
            else if (player2.getCanonicalName().equals("freezer"))
                g.drawImage(ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/Freezer" + player2.getActualEvolutionNumber() + ".png"), 900, 250, 500, 380, this);
        }

    }
}
