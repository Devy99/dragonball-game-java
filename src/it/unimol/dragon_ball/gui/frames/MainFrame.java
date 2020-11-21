package it.unimol.dragon_ball.gui.frames;

import it.unimol.dragon_ball.gui.GuiHandler;
import it.unimol.dragon_ball.gui.panels.BattleArenaPanel;
import it.unimol.dragon_ball.gui.panels.CharacterSelectionPanel;
import it.unimol.dragon_ball.gui.panels.MainScreenPanel;
import it.unimol.dragon_ball.utils.ResourcesHandler;

import javax.swing.*;
import java.awt.*;

/**
 * Singleton che gestisce le operazioni sull'interfaccia grafica.
 * <code>MainFrame</code> è un'estensione di JFrame.
 * Quando richiamato la prima volta, inizializza la finestra dell'interfaccia e le impostazioni del cursore e
 * mostra il pannello del menù principale.
 * La classe fornisce le seguenti funzionalità:
 * - creazione di una finestra per l'interazione con il programma;
 * - gestione della visualizzazione dei pannelli.
 * - inizializzazione del cursore di gioco.
 *
 * @author Alessandro Giagnorio
 */
public class MainFrame extends JFrame {
    public static final Dimension FRAME_SIZE = new Dimension(1280, 720);
    public static final int WIDTH = FRAME_SIZE.width;
    public static final int HEIGHT = FRAME_SIZE.height;

    private final String GAME_TITLE = "DragonBall: Heroes Championship";

    public static final int MAIN_SCREEN_PANEL = 0;
    public static final int CHARACTER_SELECTION_PANEL = 1;
    public static final int GAME_PANEL = 2;

    private JPanel actualPanel;

    private static MainFrame instance;

    private MainFrame() {
        super();
        initFrame();
        initCursor();
        showMainMenuPanel();
    }


    public static MainFrame getInstance() {
        if (MainFrame.instance == null)
            MainFrame.instance = new MainFrame();

        return instance;
    }

    public Dimension getFrameSize() {
        return FRAME_SIZE;
    }

    public JPanel getActualPanel() {
        return actualPanel;
    }

    private void initFrame() {
        this.setTitle(this.GAME_TITLE);
        this.setSize(this.FRAME_SIZE);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setFramePositionToCenter();

        Image applicationIcon = ResourcesHandler.getInstance().getImage("/resources/other/frame_images/GameIcon.jpg");
        this.setIconImage(applicationIcon);
    }

    private void setFramePositionToCenter() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - MainFrame.WIDTH) / 2;
        int y = (screenSize.height - MainFrame.HEIGHT) / 2;
        this.setLocation(x, y);
    }


    private void initCursor() {
        Image cursorImage = ResourcesHandler.getInstance().getImage("/resources/other/frame_images/SphereCursor.png");

        Cursor customCursor = getToolkit().createCustomCursor(cursorImage, new Point(0, 0), "Sphere Cursor");
        this.setCursor(customCursor);
    }

    private void showMainMenuPanel() {
        actualPanel = new MainScreenPanel();
        this.add(actualPanel);
    }

    /**
     * Rimuove dalla finestra il pannello attuale e imposta come pannello attivo quello selezionato tramite ID.
     * Aggiunge il nuovo pannello alla finestra per essere visualizzato.
     *
     * @param newPanelId id del pannello da visualizzare.
     */
    public void switchPanel(int newPanelId) {
        assert actualPanel != null;

        actualPanel.setVisible(false);
        this.remove(actualPanel);

        switch (newPanelId) {
            case MAIN_SCREEN_PANEL:
                actualPanel = new MainScreenPanel();
                break;

            case CHARACTER_SELECTION_PANEL:
                actualPanel = new CharacterSelectionPanel();
                break;

            case GAME_PANEL:
                actualPanel = new BattleArenaPanel();
                break;
        }

        this.add(actualPanel);
        this.revalidate();
        GuiHandler.getInstance().setButtonSelectionSoundActive(false);
    }


    /**
     * Mostra attraverso una finestra di popup un messaggio di avviso.
     * Il titolo e il messaggio da visualizzare vengono presi come parametri del metodo.
     *
     * @param popupTitle   titolo da visualizzare.
     * @param popupMessage messaggio da visualizzare.
     */
    public void showPopup(String popupTitle, String popupMessage) {
        JOptionPane.showMessageDialog(this, popupMessage, popupTitle, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mostra attraverso una finestra di popup un messaggio di errore.
     * Il messaggio da visualizzare viene preso come parametro del metodo.
     *
     * @param errorMessage messaggio di errore da visualizzare.
     */
    public void showErrorPopup(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }


}
