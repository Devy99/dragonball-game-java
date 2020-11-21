package it.unimol.dragon_ball.gui.panels;

import it.unimol.dragon_ball.gui.GuiHandler;
import it.unimol.dragon_ball.gui.custom_components.buttons.CustomButton;
import it.unimol.dragon_ball.gui.frames.MainFrame;
import it.unimol.dragon_ball.utils.ResourcesHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Classe che rappresenta il menù principale del gioco.
 * MainScreenPanel è un'estensione di JPanel.
 * Quando viene istanziato, si ha l'inizializzazione del pannello, dei bottoni e dei listener per un'interazione
 * funzionale con l'interfaccia.
 *
 * @author Alessandro
 */
public class MainScreenPanel extends JPanel {
    private final Dimension PANEL_SIZE = MainFrame.FRAME_SIZE;
    private final Dimension PANEL_LOGO = new Dimension(420, 544);

    private Image backgroundAnimation;
    private Image logoPanel;
    private Image backgroundSelectionPanel;
    private Image gokuAnimation;

    private Image aboutPanel;
    private boolean isAboutPanelSelected;

    private CustomButton computerGameButton;
    private CustomButton multiplayerButton;
    private CustomButton aboutButton;
    private CustomButton exitButton;

    private CustomButton[] buttonList;


    public MainScreenPanel() {
        initPanel();
        initButtons();
        initListeners();

        if (!GuiHandler.getInstance().isMainBackgroundMusicActive())
            initBackgroundMusic();
    }


    private void initPanel() {
        backgroundAnimation = new ImageIcon(getClass().getResource("/resources/other/main_panel_images/MenuWallpaper.gif")).getImage();
        logoPanel = ResourcesHandler.getInstance().getImage("/resources/other/main_panel_images/MenuPanel.png");
        backgroundSelectionPanel = ResourcesHandler.getInstance().getImage("/resources/other/main_panel_images/SelectionPanelBackground.png");
        gokuAnimation = new ImageIcon(getClass().getResource("/resources/other/main_panel_images/GokuAnimation.gif")).getImage();

        aboutPanel = ResourcesHandler.getInstance().getImage("/resources/other/main_panel_images/AboutPanel.png");
        isAboutPanelSelected = false;

        this.setSize(this.PANEL_SIZE);
        this.setLayout(null);
    }


    private void initButtons() {
        Image computerGameButtonImage = ResourcesHandler.getInstance().getImage("/resources/other/main_panel_images/buttons/1v1Button.png");
        Image computerGameButtonSelectedImage = ResourcesHandler.getInstance().getImage("/resources/other/main_panel_images/buttons/1v1ButtonSelected.png");

        Image multiplayerButtonImage = ResourcesHandler.getInstance().getImage("/resources/other/main_panel_images/buttons/MultiplayerButton.png");
        Image multiplayerButtonSelectedImage = ResourcesHandler.getInstance().getImage("/resources/other/main_panel_images/buttons/MultiplayerButtonSelected.png");

        Image aboutButtonImage = ResourcesHandler.getInstance().getImage("/resources/other/main_panel_images/buttons/AboutButton.png");
        Image aboutButtonSelectedImage = ResourcesHandler.getInstance().getImage("/resources/other/main_panel_images/buttons/AboutButtonSelected.png");

        Image exitButtonImage = ResourcesHandler.getInstance().getImage("/resources/other/main_panel_images/buttons/ExitButton.png");
        Image exitButtonSelectedImage = ResourcesHandler.getInstance().getImage("/resources/other/main_panel_images/buttons/ExitButtonSelected.png");


        computerGameButton = new CustomButton(computerGameButtonImage, computerGameButtonSelectedImage,
                new Point(895, 350),
                new Dimension(230, 50));

        multiplayerButton = new CustomButton(multiplayerButtonImage, multiplayerButtonSelectedImage,
                new Point(895, 420),
                new Dimension(230, 50));

        aboutButton = new CustomButton(aboutButtonImage, aboutButtonSelectedImage,
                new Point(895, 490),
                new Dimension(230, 50));

        exitButton = new CustomButton(exitButtonImage, exitButtonSelectedImage,
                new Point(895, 560),
                new Dimension(230, 50));

        buttonList = new CustomButton[]{computerGameButton, multiplayerButton, aboutButton, exitButton};

    }

    private void initBackgroundMusic() {
        GuiHandler.getInstance().setMainBackgroundMusicActive(true);
        GuiHandler.getInstance().startMainBackgroundMusic();
    }


    private void initListeners() {
        GuiHandler.getInstance().handleButtonsMouseMotionListener(this.buttonList, this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (computerGameButton.containsPoint(e.getPoint())) {
                    MainFrame.getInstance().showPopup("Info","Coming soon!");
                    /* COMING SOON
                    GuiHandler.getInstance().setComputerGameActive();
                    MainFrame.getInstance().switchPanel(MainFrame.CHARACTER_SELECTION_PANEL);
                    */
                }

                if (multiplayerButton.containsPoint(e.getPoint())) {
                    GuiHandler.getInstance().setMultiplayerGameActive();
                    MainFrame.getInstance().switchPanel(MainFrame.CHARACTER_SELECTION_PANEL);
                }

                if(aboutButton.containsPoint(e.getPoint())){
                    isAboutPanelSelected = true;
                    repaint();
                }else{
                    isAboutPanelSelected = false;
                    repaint();
                }

                if (exitButton.containsPoint(e.getPoint()))
                    System.exit(0);
            }
        });


    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundAnimation, 0, -12, MainFrame.WIDTH, MainFrame.HEIGHT + 12, this);
        g.drawImage(backgroundSelectionPanel, 800, 100, this.PANEL_LOGO.width, this.PANEL_LOGO.height, this);
        g.drawImage(logoPanel, 800, 100, this.PANEL_LOGO.width, this.PANEL_LOGO.height, this);

        computerGameButton.draw(g);
        multiplayerButton.draw(g);
        aboutButton.draw(g);
        exitButton.draw(g);

        // This part of code is activated when user select a button: draws goku animation at left of the button.
        CustomButton lastSelectedButton = GuiHandler.getInstance().getLastSelectedButton();
        if (lastSelectedButton != null && Arrays.asList(buttonList).contains(lastSelectedButton)) {
            g.drawImage(gokuAnimation, lastSelectedButton.getPositionX() - 100, lastSelectedButton.getPositionY() - 35, 150, 120, this);
        }

        if(isAboutPanelSelected)
            g.drawImage(aboutPanel, 300, 50, 600, 600, this);
    }
}
