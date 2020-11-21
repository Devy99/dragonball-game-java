package it.unimol.dragon_ball.gui.custom_components;

import it.unimol.dragon_ball.app.logic.Character;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Classe astratta che descrive un generico Sprite rappresentabile su di un pannello.
 * Inizializzare uno Sprite richiede un'istanza di {@link Character}, una posizione {@link Point}, una dimensione
 * {@link Dimension}, un pannello {@link JPanel} e un booleano che indica se lo sprite deve essere capovolto da destra
 * verso sinistra (true) o no (false).
 */
public abstract class Sprite {
    private Image activeSprite;
    private Character character;
    private Point position;
    private Dimension dimension;
    private JPanel actualPanel;
    private boolean isFlipped;
    private AtomicBoolean isAnimationActive;

    public Sprite(Character character, Point position, Dimension dimension, JPanel actualPanel, boolean isFlipped) {
        this.character = character;
        this.position = position;
        this.dimension = dimension;
        this.actualPanel = actualPanel;
        this.isFlipped = isFlipped;

        this.isAnimationActive = new AtomicBoolean(false);
    }

    public Sprite() {
    }

    public JPanel getActualPanel() {
        return actualPanel;
    }

    public Image getActiveSprite() {
        return activeSprite;
    }

    public void setActiveSprite(Image activeSprite) {
        this.activeSprite = activeSprite;
    }

    public Character getCharacter() {
        return character;
    }

    public Point getPosition() {
        return position;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public AtomicBoolean getIsAnimationActive() {
        return isAnimationActive;
    }

    public void setIsAnimationActive(boolean isAnimationActive) {
        this.isAnimationActive.set(isAnimationActive);
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public abstract void draw(Graphics g);

}
