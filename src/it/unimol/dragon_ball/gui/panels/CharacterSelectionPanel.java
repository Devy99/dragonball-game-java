package it.unimol.dragon_ball.gui.panels;

import it.unimol.dragon_ball.app.GameController;
import it.unimol.dragon_ball.gui.GuiHandler;
import it.unimol.dragon_ball.gui.custom_components.buttons.CustomButton;
import it.unimol.dragon_ball.gui.frames.MainFrame;
import it.unimol.dragon_ball.utils.ResourcesHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

/**
 * Classe che rappresenta il pannello di selezione dei personaggi.
 * CharacterSelectionPanel è un'estensione di JPanel.
 * Quando viene istanziato, si ha l'inizializzazione del pannello, dei bottoni e dei listener per un'interazione
 * funzionale con l'interfaccia.
 *
 * @author Alessandro
 */
public class CharacterSelectionPanel extends JPanel {

    private final Dimension PANEL_SIZE = MainFrame.FRAME_SIZE;

    private Image backgroundAnimation;
    private Image logoPanel;
    private Image selectLogo;
    private Image mapLogo;

    private CustomButton backButton;
    private CustomButton startGameButton;

    private Image firstPlayerLogo;
    private Image secondPlayerLogo;
    private Image charactersSelectionSquare;

    private Image character1Border;
    private CustomButton character1Button;
    private CustomButton previousCharacter1Button;
    private Image character2Border;
    private CustomButton character2Button;
    private CustomButton previousCharacter2Button;

    private Image mapBorder;
    private CustomButton selectedMapButton;
    private String selectedMapFilepath;

    private Image gokuButtonImage;
    private Image gokuButtonImageSelected;
    private Image gokuAnimation;
    private Image gokuStatsImage;
    private Image freezerButtonImage;
    private Image freezerButtonImageSelected;
    private Image freezerAnimation;
    private Image freezerStatsImage;
    private Image statsContainer1Image;
    private Image statsContainer2Image;
    private Image activeAnimation1;
    private Image activeCharacter1Stats;
    private Image activeAnimation2;
    private Image activeCharacter2Stats;

    private CustomButton goku1Button;
    private CustomButton freezer1Button;
    private CustomButton unknown1Button;
    private CustomButton unknown2Button;

    private CustomButton goku2Button;
    private CustomButton freezer2Button;
    private CustomButton unknown3Button;
    private CustomButton unknown4Button;

    private CustomButton gokuHouseMapButton;
    private CustomButton mountainsMapButton;
    private CustomButton namecPlanetMapButton;
    private CustomButton worldTournamentStageMapButton;

    private CustomButton selectedButton;
    private CustomButton[] buttonList;
    private CustomButton[] charactersButtonList;
    private CustomButton[] unknownCharactersButtonList;
    private CustomButton[] mapsButtonList;

    private boolean isCharacter1Selected;
    private boolean isCharacter2Selected;
    private boolean isMapSelected;

    public CharacterSelectionPanel() {
        initPanel();
        initButtons();
        initListeners();
    }


    private void initPanel() {
        backgroundAnimation = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/SelectionPanelBackground.jpg");
        logoPanel = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/titles/CharacterSelectionLogo.png");

        statsContainer1Image = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/StatsContainer1.png");
        statsContainer2Image = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/StatsContainer2.png");
        gokuAnimation =  new ImageIcon(getClass().getResource("/resources/other/character_selection_images/GokuGif/GokuGif.gif")).getImage();
        gokuStatsImage = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/GokuStats.png");
        freezerAnimation =  new ImageIcon(getClass().getResource("/resources/other/character_selection_images/FreezerGif/FreezerGif.gif")).getImage();
        freezerStatsImage = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/FreezerStats.png");


        selectLogo = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/titles/SelectLogo.png");
        mapLogo = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/titles/MapLogo.png");

        if (GuiHandler.getInstance().isComputerGameActive()) {
            firstPlayerLogo = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/titles/YouLogo.png");
            secondPlayerLogo = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/titles/EnemyLogo.png");
        } else if (GuiHandler.getInstance().isMultiplayerGameActive()) {
            firstPlayerLogo = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/titles/Player1Logo.png");
            secondPlayerLogo = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/titles/Player2Logo.png");
        }

        charactersSelectionSquare = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/CharacterSelectionSquare.png");

        character1Border = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/Character1Border.png");
        character2Border = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/Character2Border.png");
        mapBorder = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/MapBorder.png");

        this.setSize(PANEL_SIZE);
        this.setLayout(null);
    }

    private void initButtons() {

        Image backButtonImage = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/backButton.png");
        Image backButtonSelectedImage = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/backButtonSelected.png");

        Image startGameButtonImage = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/startGameButton.png");
        Image startGameButtonSelectedImage = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/startGameButtonSelected.png");

        gokuButtonImage = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/GokuButton.png");
        Image gokuButtonImageSelect = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/GokuButtonSelect.png");
        gokuButtonImageSelected = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/GokuButtonSelected.png");

        freezerButtonImage = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/FreezerButton.png");
        Image freezerButtonImageSelect = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/FreezerButtonSelect.png");
        freezerButtonImageSelected = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/FreezerButtonSelected.png");

        Image UnknownButtonImage = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/UnknownButton.png");
        Image UnknownButtonImageSelect = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/UnknownButtonSelected.png");

        Image gokuHouseMapButtonImage = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/GokuHouseMapButton.png");
        Image gokuHouseMapButtonImageSelected = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/GokuHouseMapButtonSelected.png");

        Image mountainsMapButtonImage = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/MountainsMapButton.png");
        Image mountainsMapButtonImageSelected = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/MountainsMapButtonSelected.png");

        Image namecPlanetMapButtonImage = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/NamecPlanetMapButton.png");
        Image namecPlanetMapButtonImageSelected = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/NamecPlanetMapButtonSelected.png");

        Image worldTournamentStageMapButtonImage = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/WorldTournamentStageMapButton.png");
        Image worldTournamentStageMapButtonImageSelected = ResourcesHandler.getInstance().getImage("/resources/other/character_selection_images/buttons/WorldTournamentStageMapButtonSelected.png");

        backButton = new CustomButton(backButtonImage, backButtonSelectedImage,
                new Point(50, -40),
                new Dimension(200, 200));

        startGameButton = new CustomButton(startGameButtonImage, startGameButtonSelectedImage,
                new Point(1030, -40),
                new Dimension(200, 200));

        goku1Button = new CustomButton(gokuButtonImage, gokuButtonImageSelect,
                new Point(50, 277),
                new Dimension(94, 144));

        freezer1Button = new CustomButton(freezerButtonImage, freezerButtonImageSelect,
                new Point(150, 277),
                new Dimension(94, 144));

        unknown1Button = new CustomButton(UnknownButtonImage, UnknownButtonImageSelect,
                new Point(50, 425),
                new Dimension(94, 144));

        unknown2Button = new CustomButton(UnknownButtonImage, UnknownButtonImageSelect,
                new Point(150, 425),
                new Dimension(94, 144));


        goku2Button = new CustomButton(gokuButtonImage, gokuButtonImageSelect,
                new Point(1006, 277),
                new Dimension(94, 144));

        freezer2Button = new CustomButton(freezerButtonImage, freezerButtonImageSelect,
                new Point(1106, 277),
                new Dimension(94, 144));

        unknown3Button = new CustomButton(UnknownButtonImage, UnknownButtonImageSelect,
                new Point(1006, 425),
                new Dimension(94, 144));

        unknown4Button = new CustomButton(UnknownButtonImage, UnknownButtonImageSelect,
                new Point(1106, 425),
                new Dimension(94, 144));


        gokuHouseMapButton = new CustomButton(gokuHouseMapButtonImage, gokuHouseMapButtonImageSelected,
                new Point(400, 610),
                new Dimension(133, 100));

        mountainsMapButton = new CustomButton(mountainsMapButtonImage, mountainsMapButtonImageSelected,
                new Point(583, 610),
                new Dimension(133, 100));

        namecPlanetMapButton = new CustomButton(namecPlanetMapButtonImage, namecPlanetMapButtonImageSelected,
                new Point(756, 610),
                new Dimension(133, 100));

        worldTournamentStageMapButton = new CustomButton(worldTournamentStageMapButtonImage, worldTournamentStageMapButtonImageSelected,
                new Point(939, 610),
                new Dimension(133, 100));

        buttonList = new CustomButton[]{goku1Button, freezer1Button, unknown1Button, unknown2Button,
                goku2Button, freezer2Button, unknown3Button, unknown4Button,
                gokuHouseMapButton, mountainsMapButton, namecPlanetMapButton, worldTournamentStageMapButton,
                backButton, startGameButton};

        charactersButtonList = new CustomButton[]{goku1Button, freezer1Button, unknown1Button, unknown2Button,
                goku2Button, freezer2Button, unknown3Button, unknown4Button};

        unknownCharactersButtonList = new CustomButton[]{unknown1Button, unknown2Button, unknown3Button, unknown4Button};

        mapsButtonList = new CustomButton[]{gokuHouseMapButton, mountainsMapButton, namecPlanetMapButton, worldTournamentStageMapButton};
    }


    private void initListeners() {
        GuiHandler.getInstance().handleButtonsMouseMotionListener(buttonList, this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (backButton.containsPoint(e.getPoint())) {
                    MainFrame.getInstance().switchPanel(MainFrame.MAIN_SCREEN_PANEL);
                }

                if (startGameButton.containsPoint(e.getPoint())) {
                    if(checkSelectionValidity()) {
                        GuiHandler.getInstance().setSelectedBackgroundMapFilepath(selectedMapFilepath);
                        GuiHandler.getInstance().stopBackgroundMusic();
                        GameController.getInstance().startGame();
                        MainFrame.getInstance().switchPanel(MainFrame.GAME_PANEL);
                    }
                }

                // Characters buttons handling
                selectedButton = GuiHandler.getInstance().getSelectedButton(charactersButtonList, e.getPoint());
                if (selectedButton != null) {
                    if (selectedButton.getPositionX() < PANEL_SIZE.width / 2) {
                        previousCharacter1Button = character1Button;
                        isCharacter1Selected = true;
                        character1Button = selectedButton;
                    } else if (selectedButton.getPositionX() > PANEL_SIZE.width / 2) {
                        previousCharacter2Button = character2Button;
                        isCharacter2Selected = true;
                        character2Button = selectedButton;
                    }

                    resetPreviousSelectedButton();
                    handleMainButtonImageAndCharacterSelection();
                    repaint();
                }

                // Maps buttons handling
                selectedButton = GuiHandler.getInstance().getSelectedButton(mapsButtonList, e.getPoint());
                if (selectedButton != null) {
                    isMapSelected = true;
                    selectedMapButton = selectedButton;
                    repaint();
                }
                handleSelectedMapFilepath();
            }
        });
    }

    private void resetPreviousSelectedButton() {
        if (previousCharacter1Button != null) {
            if (previousCharacter1Button.getPosition().equals(goku1Button.getPosition())) {
                goku1Button.setButtonImage(gokuButtonImage);
                goku1Button.setActiveButtonImage(gokuButtonImage);
            } else if (previousCharacter1Button.getPosition().equals(freezer1Button.getPosition())) {
                freezer1Button.setButtonImage(freezerButtonImage);
                freezer1Button.setActiveButtonImage(freezerButtonImage);
            }
        }

        if (previousCharacter2Button != null) {
            if (previousCharacter2Button.getPosition().equals(goku2Button.getPosition())) {
                goku2Button.setButtonImage(gokuButtonImage);
                goku2Button.setActiveButtonImage(gokuButtonImage);
            } else if (previousCharacter2Button.getPosition().equals(freezer2Button.getPosition())) {
                freezer2Button.setActiveButtonImage(freezerButtonImage);
                freezer2Button.setButtonImage(freezerButtonImage);
            }
        }
    }

    private void handleMainButtonImageAndCharacterSelection() {
        // This part of code is activated when the user click on the characters. Shows the text "Selected".
        if (isCharacter1Selected && character1Button.getPosition().equals(goku1Button.getPosition())) {
            goku1Button.setButtonImage(gokuButtonImageSelected);
            GameController.getInstance().setPlayer1AsGoku();
        }
        if (isCharacter2Selected && character2Button.getPosition().equals(goku2Button.getPosition())) {
            goku2Button.setButtonImage(gokuButtonImageSelected);
            GameController.getInstance().setPlayer2AsGoku();
        }
        if (isCharacter1Selected && character1Button.getPosition().equals(freezer1Button.getPosition())) {
            freezer1Button.setButtonImage(freezerButtonImageSelected);
            GameController.getInstance().setPlayer1AsFreezer();
        }
        if (isCharacter2Selected && character2Button.getPosition().equals(freezer2Button.getPosition())) {
            freezer2Button.setButtonImage(freezerButtonImageSelected);
            GameController.getInstance().setPlayer2AsFreezer();
        }
    }

    private boolean checkSelectionValidity() {
        if (character1Button == null) {
            MainFrame.getInstance().showErrorPopup("Player1 character not selected!");
            return false;

        } else if (character2Button == null) {
            MainFrame.getInstance().showErrorPopup("Player2 character not selected!");
            return false;

        } else if (Arrays.asList(unknownCharactersButtonList).contains(character1Button) ||
                Arrays.asList(unknownCharactersButtonList).contains(character2Button)) {
            MainFrame.getInstance().showErrorPopup("Cannot select coming soon characters!");
            return false;

        } else if (selectedMapButton == null) {
            MainFrame.getInstance().showErrorPopup("Map not selected!");
            return false;
        }
        return true;
    }


    private void handleSelectedMapFilepath() {
        if (isMapSelected && selectedMapButton.getPosition().equals(gokuHouseMapButton.getPosition()))
            selectedMapFilepath = "/resources/maps/GokuHouse.jpg";

        if (isMapSelected && selectedMapButton.getPosition().equals(mountainsMapButton.getPosition()))
            selectedMapFilepath = "/resources/maps/Mountains.jpg";

        if (isMapSelected && selectedMapButton.getPosition().equals(namecPlanetMapButton.getPosition()))
            selectedMapFilepath = "/resources/maps/NamecPlanet.jpg";

        if (isMapSelected && selectedMapButton.getPosition().equals(worldTournamentStageMapButton.getPosition()))
            selectedMapFilepath = "/resources/maps/WorldTournamentStage.jpg";
    }

    public String getSelectedMapFilepath() {
        return selectedMapFilepath;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundAnimation, 0, 0, this.PANEL_SIZE.width, this.PANEL_SIZE.height, this);
        g.drawImage(logoPanel, 390, 25, 500, 50, this);

        backButton.draw(g);
        startGameButton.draw(g);

        assert firstPlayerLogo != null;
        assert secondPlayerLogo != null;

        /* Different texts are shown depending on the modality chosen by the user.*/
        if (GuiHandler.getInstance().isComputerGameActive()) {
            g.drawImage(firstPlayerLogo, 100, 130, 102, 51, this);
            g.drawImage(secondPlayerLogo, 1015, 130, 165, 51, this);
        } else if (GuiHandler.getInstance().isMultiplayerGameActive()) {
            g.drawImage(firstPlayerLogo, 65, 130, 193, 53, this);
            g.drawImage(secondPlayerLogo, 1015, 130, 200, 53, this);
        }
        /* */

        g.drawImage(charactersSelectionSquare, 20, 200, 260, 384, this);
        g.drawImage(charactersSelectionSquare, 970, 200, 260, 384, this);


        goku1Button.draw(g);
        freezer1Button.draw(g);
        unknown1Button.draw(g);
        unknown2Button.draw(g);

        goku2Button.draw(g);
        freezer2Button.draw(g);
        unknown3Button.draw(g);
        unknown4Button.draw(g);



        /* This part of code is activated when the user select the characters. Draws the selection border. */
        if (this.isCharacter1Selected && this.character1Button != null) {
            int x1 = character1Button.getPositionX();
            int y1 = character1Button.getPositionY();
            int width1 = character1Button.getWidth();
            int height1 = character1Button.getHeight();

            if (this.character1Button.getPosition().equals(goku1Button.getPosition())) {
                activeAnimation1 = gokuAnimation;
                activeCharacter1Stats = gokuStatsImage;
            } else if (this.character1Button.getPosition().equals(freezer1Button.getPosition())) {
                activeAnimation1 = freezerAnimation;
                activeCharacter1Stats = freezerStatsImage;
            }

            g.drawImage(character1Border, x1, y1, width1, height1, this);

            // If the button selected is an unknown character, don't shown animation and stats of character.
            if (!Arrays.asList(unknownCharactersButtonList).contains(character1Button)) {
                g.drawImage(statsContainer1Image, 300, 370, 430, 200, this);
                g.drawImage(activeAnimation1, 160, 210, 500, 380, this);
                g.drawImage(activeCharacter1Stats, 535, 440, 140, 140, this);
            }
        }

        if (this.isCharacter2Selected && this.character2Button != null) {
            int width2 = character2Button.getWidth();
            int height2 = character2Button.getHeight();
            int x2 = character2Button.getPositionX();
            int y2 = character2Button.getPositionY();

            if (this.character2Button.getPosition().equals(goku2Button.getPosition())) {
                activeAnimation2 = gokuAnimation;
                activeCharacter2Stats = gokuStatsImage;
            } else if (this.character2Button.getPosition().equals(freezer2Button.getPosition())) {
                activeAnimation2 = freezerAnimation;
                activeCharacter2Stats = freezerStatsImage;
            }

            g.drawImage(character2Border, x2, y2, width2, height2, this);

            // If the button selected is an unknown character, don't shown animation and stats of character.
            if (!Arrays.asList(unknownCharactersButtonList).contains(character2Button)) {
                g.drawImage(statsContainer2Image, 520, 240, 430, 200, this);
                g.drawImage(activeAnimation2, 595, 210, 500, 380, this);
                g.drawImage(activeCharacter2Stats, 570, 240, 140, 140, this);
            }
        }
        /* */


        g.drawImage(selectLogo, 110, 640, 154, 50, this);
        g.drawImage(mapLogo, 260, 643, 126, 45, this);

        gokuHouseMapButton.draw(g);
        mountainsMapButton.draw(g);
        namecPlanetMapButton.draw(g);
        worldTournamentStageMapButton.draw(g);

        /* This part of code is activated when the user select the maps. Draws the selection border. */
        if (this.isMapSelected && this.selectedMapButton != null) {
            int width3 = selectedMapButton.getWidth();
            int x3 = selectedMapButton.getPositionX();
            int y3 = selectedMapButton.getPositionY();
            int height3 = selectedMapButton.getHeight();

            g.drawImage(mapBorder, x3, y3 + 10, width3, height3 - 20, this);
        }
        /* */
    }
}
