package it.unimol.dragon_ball.gui.custom_components.animations;

import it.unimol.dragon_ball.gui.custom_components.Sprite;
import it.unimol.dragon_ball.gui.custom_components.sprites_classes.CharacterSprite;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread che gestisce le animazioni degli {@link Sprite}.
 * Inizializzarlo richiede un'istanza di {@link Sprite}, una lista di immagini rappresentati i singoli frame dell'animazione,
 * un'istanza di {@link JPanel} rappresentante il pannello su cui disegnare quest'animazione, un'istanza di {@link AtomicBoolean}
 * che gestisce il tempo di attività dell'animazione e un'intero che descrive l'intervallo di tempo che separa la visualizzazione
 * di un frame dal successivo.
 */
public class SpriteAnimation extends Thread {
    private Sprite sprite;
    private ArrayList<Image> animationFrames;
    private JPanel actualPanel;
    private AtomicBoolean isActive;
    private int sleepTime;

    public SpriteAnimation(Sprite sprite, ArrayList<Image> animationFrames, JPanel actualPanel, AtomicBoolean isActive, int sleepTime) {
        super();
        this.sprite = sprite;
        this.animationFrames = animationFrames;
        this.actualPanel = actualPanel;
        this.isActive = isActive;
        this.sleepTime = sleepTime;
    }


    @Override
    public void run() {
        int i = 0;
        while (isActive.get()) {
            // Durante l'update degli sprite potrebbe essere eseguita l'animazione nonostante la lista sia vuota.
            if (animationFrames.size() != 0) {
                if (i == animationFrames.size())
                    i = 0;

                sprite.setActiveSprite(animationFrames.get(i));
                actualPanel.repaint();
                i++;

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
