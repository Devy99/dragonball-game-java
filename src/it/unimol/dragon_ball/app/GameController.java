package it.unimol.dragon_ball.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import it.unimol.dragon_ball.app.logic.Character;
import it.unimol.dragon_ball.app.logic.Skill;

/**
 * Singleton che gestisce il flusso di gioco.
 *      Questa classe fornisce le seguenti funzionalità:
 *          - avvio e interruzione del flusso di gioco;
 *          - selezione dei personaggi giocanti;
 *          - aggiornamento controllato dei dati dei personaggi;
 *          - restituisce il nome del player vincitore a fine partita.
 */
public class GameController {
    private Character player1;
    private boolean isCharacter1Goku;
    private boolean isCharacter1Freezer;

    private Character player2;
    private boolean isCharacter2Goku;
    private boolean isCharacter2Freezer;

    private String winnerName;

    private volatile boolean isGameActive;

    private static GameController instance;

    private GameController() {
        isGameActive = false;

        isCharacter1Goku = false;
        isCharacter1Freezer = false;
        isCharacter2Goku = false;
        isCharacter2Freezer = false;
    }

    public static GameController getInstance() {
        if (GameController.instance == null)
            GameController.instance = new GameController();

        return instance;
    }


    public void setPlayer1AsGoku() {
        this.isCharacter1Goku = true;
        this.isCharacter1Freezer = false;
    }

    public void setPlayer2AsGoku() {
        this.isCharacter2Goku = true;
        this.isCharacter2Freezer = false;
    }

    public void setPlayer1AsFreezer() {
        this.isCharacter1Goku = false;
        this.isCharacter1Freezer = true;
    }

    public void setPlayer2AsFreezer() {
        this.isCharacter2Goku = false;
        this.isCharacter2Freezer = true;
    }

    /**
     * Avvia il flusso di gioco.
     * Vengono inizializzati i personaggi selezionati e viene avviato il thread che aggiorna automaticamente
     * i dati dei personaggi al verificarsi degli eventi.
     */
    public void startGame() {
        this.isGameActive = true;
        initPlayers();
        startGameThread();
    }

    private void startGameThread() {
        Thread gameBattleThread = new Thread(() -> {

            while (this.isGameActive) {
                /* Player 1 handling */
                if (this.player1.getAura() >= this.player1.getMaxAura() &&
                        this.player1.getActualEvolutionNumber() < this.player1.getMAX_EVOLUTIONS_NUMBER()) {
                        player1.resetAura();
                        player1.evolve();
                }
                /* */


                /* Player 2 handling */
                if (this.player2.getAura() >= this.player2.getMaxAura() &&
                        this.player2.getActualEvolutionNumber() < this.player2.getMAX_EVOLUTIONS_NUMBER()) {
                        player2.resetAura();
                        player2.evolve();
                }

                /* */

                if (this.player1.getActualHP() < 0) {
                    winnerName = "player2";
                    stopGame();
                } else if (this.player2.getActualHP() < 0) {
                    winnerName = "player1";
                    stopGame();
                }

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        gameBattleThread.start();
    }

    public void stopGame() {
        this.isGameActive = false;
    }


    private void initPlayers() {
        String character1PropertiesFilepath = null;
        String character2PropertiesFilepath = null;

        if (isCharacter1Goku)
            character1PropertiesFilepath = "/resources/file_properties/goku.properties";
        else if (isCharacter1Freezer)
            character1PropertiesFilepath = "/resources/file_properties/freezer.properties";

        if (isCharacter2Goku)
            character2PropertiesFilepath = "/resources/file_properties/goku.properties";
        else if (isCharacter2Freezer)
            character2PropertiesFilepath = "/resources/file_properties/freezer.properties";

        assert character1PropertiesFilepath != null && character2PropertiesFilepath != null;
        try (InputStream inputStream1 = getClass().getResourceAsStream(character1PropertiesFilepath);
             InputStream inputStream2 = getClass().getResourceAsStream(character2PropertiesFilepath)) {
            Properties propertiesCharacter1 = new Properties();
            propertiesCharacter1.load(inputStream1);
            player1 = new Character(propertiesCharacter1);

            Properties propertiesCharacter2 = new Properties();
            propertiesCharacter2.load(inputStream2);
            player2 = new Character(propertiesCharacter2);

            initPlayersSkills();
        } catch (IOException ignored) {

        }

    }

    private void initPlayersSkills() {
        if (isCharacter1Goku)
            initGokuSkills(player1);
        else if (isCharacter1Freezer)
            initFreezerSkills(player1);

        if (isCharacter2Goku)
            initGokuSkills(player2);
        else if (isCharacter2Freezer)
            initFreezerSkills(player2);
    }

    private void initGokuSkills(Character goku) {
        goku.addNewSkill(new Skill("Punch", goku, 0, 0, 0));
        goku.addNewSkill(new Skill("EnergyBall", goku, 30, 75, 0.5));
        goku.addNewSkill(new Skill("Kamehameha", goku,  150, 400, 10));
        goku.addNewSkill(new Skill("GenkidamaSphere", goku, 300, 800, 25));
    }

    private void initFreezerSkills(Character freezer) {
        freezer.addNewSkill(new Skill("Punch", freezer, 0, 0, 0));
        freezer.addNewSkill(new Skill("EnergyBall", freezer,  40, 50, 0.5));
        freezer.addNewSkill(new Skill("DeathBullet", freezer, 200, 350, 15));
        freezer.addNewSkill(new Skill("SupernovaSphere", freezer, 400, 700, 30));
    }


    public Character getPlayer1() {
        return player1;
    }

    public Character getPlayer2() {
        return player2;
    }


    public boolean isGameActive() {
        return isGameActive;
    }

    public String getWinnerName() {
        return winnerName;
    }
}
