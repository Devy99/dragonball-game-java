package it.unimol.dragon_ball.gui.custom_components.bars;

import java.awt.*;
import java.awt.image.BufferedImage;

import it.unimol.dragon_ball.app.logic.Character;
import it.unimol.dragon_ball.utils.ResourceException;
import it.unimol.dragon_ball.utils.ResourcesHandler;

import javax.swing.*;

/**
 * Classe che rappresenza sul pannello la StatusBar del personaggio, ovvero la sezione contenente informazioni circa la vita,
 * il mana e l'aura.
 * Inizializzarlo richiede un'istanza di {@link Character}, la posizione {@link Point} all'interno del pannello e un
 * booleano che descrive se la raffigurazione deve essere capovolta da destra verso sinistra (true) o no (false).
 */
public class StatusBar {
    private Image characterIconCircle;
    private Image characterIcon;
    private Image characterStatus;
    private Image statusBarInfo;
    private Image characterNameImage;

    private BufferedImage[] hpBarList;
    private BufferedImage mpBar;
    private BufferedImage auraBar;

    private Character character;

    private final int SINGLE_BAR_HP;
    private int hp;
    private int mp;
    private int aura;

    private Point position;
    private boolean isFlipped;

    public StatusBar(Character character, Point position, boolean isFlipped) {
        this.character = character;

        this.hp = character.getActualHP();
        this.mp = character.getActualMP();
        this.position = position;
        this.isFlipped = isFlipped;
        this.aura = 1;

        this.SINGLE_BAR_HP = 300;

        initStatusBar();
    }

    private void initStatusBar() {
        characterIconCircle = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/status_bar/CharacterNameAndIconImage.png");
        characterStatus = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/status_bar/CharacterStatusBar.png");
        statusBarInfo = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/status_bar/StatusBarInfo.png");

        mpBar = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/status_bar/mp.png");
        auraBar = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/status_bar/aura.png");

        BufferedImage orangeHealth = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/status_bar/hp_0.png");
        BufferedImage yellowHealth = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/status_bar/hp_1.png");
        BufferedImage greenHealth = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/status_bar/hp_2.png");
        BufferedImage lightBlueHealth = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/status_bar/hp_3.png");
        BufferedImage blueHealth = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/status_bar/hp_4.png");
        BufferedImage violetHealth = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/status_bar/hp_5.png");
        BufferedImage fuchsiaHealth = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/status_bar/hp_6.png");

        hpBarList = new BufferedImage[]{orangeHealth, yellowHealth, greenHealth, lightBlueHealth, blueHealth, violetHealth, fuchsiaHealth};


        String iconFilepath = "/resources/sprites/characters/" + this.character.getCanonicalName() + "/" + this.character.getActualEvolutionName() + "/ICON.png";
        characterIcon = ResourcesHandler.getInstance().getImage(iconFilepath);

        if (character.getCanonicalName().equals("goku"))
            characterNameImage = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/status_bar/GokuNamePanel.png");
        else if (character.getCanonicalName().equals("freezer"))
            characterNameImage = ResourcesHandler.getInstance().getImage("/resources/other/battle_panel_images/status_bar/FreezerNamePanel.png");
    }


    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getAura() {
        return aura;
    }

    public void setAura(int aura) {
        this.aura = aura;
    }

    public void draw(Graphics g) {
        this.hp = character.getActualHP();
        this.mp = character.getActualMP();
        this.aura = character.getAura();

        /* Gestione della lunghezza di taglio dell'ultima barra di vita */
        int healthBarsNumber = (int) Math.ceil(hp / (this.SINGLE_BAR_HP * 1.0));  // Approssimo per eccesso il risultato
        int hpBarWidth = ((this.hp - ((healthBarsNumber - 1) * this.SINGLE_BAR_HP)) * 127) / this.SINGLE_BAR_HP;

        if (hpBarWidth == 0)  // Per evitare che il crop generi eccezioni
            hpBarWidth++;
        /* */

        /* Gestione della lunghezza di taglio della barra del mana */
        int manaBarWidth = (this.mp * 122) / this.character.getActualMaxMp();  //122px è la larghezza reale dell'immagine
        if (manaBarWidth == 0)  // Per evitare che il crop generi eccezioni
            manaBarWidth++;

        BufferedImage croppedManaBar = mpBar.getSubimage(0, 0, manaBarWidth, 6);
        /* */

        /* Gestione della lunghezza di taglio della barra dell'aura */
        int auraBarWidth = (this.aura * 81) / this.character.getMaxAura();  //81px è la larghezza reale dell'immagine
        if (auraBarWidth == 0)  // Per evitare che il crop generi eccezioni
            auraBarWidth++;

        BufferedImage croppedAuraBar = auraBar.getSubimage(0, 0, auraBarWidth, 4);
        /* */


        String iconFilepath = "/resources/sprites/characters/" + this.character.getCanonicalName() + "/" + this.character.getActualEvolutionName() + "/ICON.png";
        characterIcon = ResourcesHandler.getInstance().getImage(iconFilepath);

        if (this.isFlipped) {
            g.drawImage(characterStatus, (int) position.getX(), (int) position.getY() + 5, -302, 110, null);

            /* Rappresentazione della barra di vita */
            for (int i = 0; i < healthBarsNumber; i++) {

                if (i == healthBarsNumber - 1) { // se devo disegnare l'ultima barra di vita...
                    BufferedImage croppedBar = hpBarList[i].getSubimage(0, 0, hpBarWidth, 11);
                    g.drawImage(croppedBar, (int) position.getX() - 12, (int) position.getY() + 25, (int) (-hpBarWidth * 2.2), 24, null);
                } else
                    g.drawImage(hpBarList[i], (int) position.getX() - 12, (int) position.getY() + 25, -279, 24, null);
            }
            /* */

            /* Rappresentazione della barra del mana */
            g.drawImage(croppedManaBar, (int) position.getX() - 11, (int) position.getY() + 66, (int) -(manaBarWidth * 2.2), 14, null);
            /* */

            /* Rappresentazione della barra dell'aura */
            g.drawImage(croppedAuraBar, (int) position.getX() - 11, (int) position.getY() + 95, (int) -(auraBarWidth * 3.3), 10, null);
            /* */

            g.drawImage(characterIcon, (int) position.getX() + 99, (int) position.getY() + 16, -108, 121, null);
            g.drawImage(characterIconCircle, (int) position.getX() - 25, (int) position.getY(), 140, 139, null);
            g.drawImage(characterNameImage, (int) position.getX() - 7, (int) position.getY() + 114, 103, 24, null);
            g.drawImage(statusBarInfo, (int) position.getX() - 50, (int) position.getY() + 10, 50, 90, null);

        } else {
            g.drawImage(characterStatus, (int) position.getX() + 110, (int) position.getY() + 5, 302, 110, null);

            /* Rappresentazione della barra di vita */
            for (int i = 0; i < healthBarsNumber; i++) {

                if (i == healthBarsNumber - 1) { // se devo disegnare l'ultima barra di vita...
                    BufferedImage croppedBar = hpBarList[i].getSubimage(0, 0, hpBarWidth, 11);
                    g.drawImage(croppedBar, (int) position.getX() + 122, (int) position.getY() + 25, (int) (hpBarWidth * 2.2), 24, null);
                } else
                    g.drawImage(hpBarList[i], (int) position.getX() + 122, (int) position.getY() + 25, 279, 24, null);
            }
            /* */

            /* Rappresentazione della barra del mana */
            g.drawImage(croppedManaBar, (int) position.getX() + 121, (int) position.getY() + 66, (int) (manaBarWidth * 2.2), 14, null);
            /* */


            /* Rappresentazione della barra dell'aura */
            g.drawImage(croppedAuraBar, (int) position.getX() + 121, (int) position.getY() + 95, (int) (auraBarWidth * 3.3), 10, null);
            /* */

            g.drawImage(characterIcon, (int) position.getX() + 15, (int) position.getY() + 17, 108, 121, null);
            g.drawImage(characterIconCircle, (int) position.getX(), (int) position.getY(), 140, 139, null);
            g.drawImage(characterNameImage, (int) position.getX() + 17, (int) position.getY() + 113, 103, 24, null);
            g.drawImage(statusBarInfo, (int) position.getX() + 115, (int) position.getY() + 10, 50, 90, null);
        }


    }

}
