package it.unimol.dragon_ball.gui.custom_components.buttons;

import java.awt.*;


/**
 * Classe che consente la rappresentazione di un bottone personalizzato:
 * presenta un'immagine di default e un'altra che può essere attivata quando il tasto viene selezionato.
 * Quando viene istanziato, viene automaticamente inizializzato il rettangolo che racchiude il bottone e
 * l'immagine principale.
 *
 * @author Alessandro
 */
public class CustomButton {

    private Image activeButtonImage;
    private Image buttonImage;

    private Image buttonSelectedImage;
    private Rectangle buttonShape;

    private Point position;
    private Dimension dimension;


    public CustomButton(Image buttonImage, Image buttonSelectedImage, Point position, Dimension dimension) {
        this.buttonImage = buttonImage;
        this.buttonSelectedImage = buttonSelectedImage;

        this.position = position;
        this.dimension = dimension;

        this.buttonShape = new Rectangle(this.position, this.dimension);

        this.activeButtonImage = buttonImage;
    }


    public Image getButtonImage() {
        return this.buttonImage;
    }

    public Image getButtonSelectedImage() {
        return buttonSelectedImage;
    }

    public void setButtonSelectedImage(Image buttonSelectedImage) {
        this.buttonSelectedImage = buttonSelectedImage;
    }

    public void setActiveButtonImage(Image image) {
        this.activeButtonImage = image;
    }

    public void setButtonImage(Image buttonImage) {
        this.buttonImage = buttonImage;
    }

    public Point getPosition() {
        return position;
    }

    public int getPositionX() {
        return (int) this.position.getX();
    }

    public int getPositionY() {
        return (int) this.position.getY();
    }


    public int getWidth() {
        return (int) this.dimension.getWidth();
    }

    public int getHeight() {
        return (int) this.dimension.getHeight();
    }


    public void draw(Graphics g) {

        g.drawImage(this.activeButtonImage, getPositionX(), getPositionY(), getWidth(), getHeight(), null);
    }

    /**
     * Metodo booleano che consente di capire se un dato punto è contenuto nel bottone.
     * In questo modo, è possibile capire se il bottone è stato selezionato.
     * @param pointClicked punto preso come parametro.
     * @return true se il bottone contiene il punto preso in input, false altrimenti.
     */
    public boolean containsPoint(Point pointClicked) {

        return this.buttonShape.contains(pointClicked);
    }


}
