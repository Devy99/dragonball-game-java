package it.unimol.dragon_ball.gui;

import it.unimol.dragon_ball.gui.custom_components.buttons.CustomButton;
import it.unimol.dragon_ball.utils.ResourceException;
import it.unimol.dragon_ball.utils.ResourcesHandler;
import it.unimol.dragon_ball.utils.SoundException;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Singleton che gestisce l'interazione tra utente e interfaccia grafica.
 *      Presenta le seguenti funzionalità:
 *       - attivazione e rimozione della musica di sottofondo;
 *       - gestione del suono dei tasti;
 *       - gestione della selezione dei tasti;
 *       - comunicazione tra i diversi pannelli esistenti;
 *
 * Di default, setta i flag utilizzati per la gestione del suono e dei tasti a false.
 *
 * @author Alessandro
 */
public class GuiHandler {
    private boolean computerGameActive;
    private boolean multiplayerGameActive;

    private boolean mainBackgroundMusicActive;
    private Clip mainBackgroundMusicClip;

    private boolean buttonSelectionSoundActive;

    private CustomButton previousSelectedButton;
    private CustomButton lastSelectedButton;

    private String selectedBackgroundMapFilepath;

    private static GuiHandler instance;


    private GuiHandler() {
        this.mainBackgroundMusicActive = false;
        this.buttonSelectionSoundActive = false;

        this.computerGameActive = false;
        this.multiplayerGameActive = false;
    }

    public static GuiHandler getInstance() {
        if (GuiHandler.instance == null)
            GuiHandler.instance = new GuiHandler();

        return instance;
    }

    public boolean isButtonSelectionSoundActive() {
        return buttonSelectionSoundActive;
    }

    public void setButtonSelectionSoundActive(boolean buttonSelectionSoundActive) {
        this.buttonSelectionSoundActive = buttonSelectionSoundActive;
    }

    public boolean isMainBackgroundMusicActive() {
        return mainBackgroundMusicActive;
    }

    public void setMainBackgroundMusicActive(boolean mainBackgroundMusicActive) {
        this.mainBackgroundMusicActive = mainBackgroundMusicActive;
    }

    public void startMainBackgroundMusic() {
        Thread thread = new Thread(() -> {
            Clip clip;

            try {
                clip = ResourcesHandler.getInstance().playMusic("/resources/sounds/other/GameIntro.wav", Clip.LOOP_CONTINUOUSLY);
                mainBackgroundMusicClip = clip;
            } catch (ResourceException ignored) {

            }

        });

        thread.start();
    }

    public void stopBackgroundMusic() {
        assert mainBackgroundMusicClip != null;

        /*
        try {
            ResourcesHandler.getInstance().stopMusic(mainBackgroundMusicClip);
        } catch (SoundException e) {
            e.printStackTrace();
        }*/

        this.mainBackgroundMusicActive = false;
    }

    private void startButtonSelectionSound() {
        Thread thread = new Thread(() -> {

            try {
                ResourcesHandler.getInstance().playMusic("/resources/sounds/other/ButtonSelectionSound1.wav", 0);
            } catch (ResourceException ignored) {

            }

        });

        thread.start();
    }


    public void setComputerGameActive() {
        this.computerGameActive = true;
        this.multiplayerGameActive = false;
    }

    public boolean isComputerGameActive() {
        return computerGameActive;
    }

    public void setMultiplayerGameActive() {
        this.computerGameActive = false;
        this.multiplayerGameActive = true;
    }

    public boolean isMultiplayerGameActive() {
        return multiplayerGameActive;
    }


    public CustomButton getLastSelectedButton() {
        if (lastSelectedButton == null)
            return null;

        return lastSelectedButton;
    }

    /**
     * Gestisce i cambiamenti del bottone al variare della posizione del cursore.
     * In particolare, quando il cursore si trova sul bottone, viene riprodotto il suono di selezione del pulsante
     * e viene settata un'altra immagine come immagine attiva del bottone.
     * @param buttons lista dei bottoni su cui effettuare le operazioni.
     * @param panel pannello su cui agisce il listener.
     */
    public void handleButtonsMouseMotionListener(CustomButton[] buttons, JPanel panel) {
        panel.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                this.mouseMoved(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                resetPreviousButton(panel);
                lastSelectedButton = getSelectedButton(buttons, e.getPoint());
                handleActiveButtonImageAndSound(panel);

                if (lastSelectedButton != null)
                    setPreviousSelectedButton(lastSelectedButton);

                if (lastSelectedButton == null)
                    buttonSelectionSoundActive = false;
            }
        });
    }

    private void resetPreviousButton(JPanel panel) {
        if (previousSelectedButton != null) {
            previousSelectedButton.setActiveButtonImage(previousSelectedButton.getButtonImage());
            panel.repaint();
        }
    }


    private void setPreviousSelectedButton(CustomButton button) {
        this.previousSelectedButton = button;
    }

    /**
     * Restituisce il bottone selezionato, ricevuto in input la lista dei bottoni da confrontare e il punto
     * di selezione.
     * @param buttons lista dei bottoni soggetti al confronto.
     * @param point punto di selezione del bottone.
     * @return null se non è stato selezionato alcun bottone, altrimenti il bottone selezionato.
     */
    public CustomButton getSelectedButton(CustomButton[] buttons, Point point) {
        for (CustomButton button : buttons) {
            if (button.containsPoint(point))
                return button;
        }

        return null;
    }

    private void handleActiveButtonImageAndSound(JPanel panel) {

        if (lastSelectedButton != null) {
            lastSelectedButton.setActiveButtonImage(lastSelectedButton.getButtonSelectedImage());
            panel.repaint();

            if (!this.buttonSelectionSoundActive) {
                //this.startButtonSelectionSound();
                this.buttonSelectionSoundActive = true;
            }
        }

    }

    public String getSelectedBackgroundMapFilepath() {
        return selectedBackgroundMapFilepath;
    }

    public void setSelectedBackgroundMapFilepath(String selectedBackgroundMap) {
        this.selectedBackgroundMapFilepath = selectedBackgroundMap;
    }


}
