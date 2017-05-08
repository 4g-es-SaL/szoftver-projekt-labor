import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;

/**
 * Makes the program run.
 */
public class Organizer extends Application {

    private final int timeBetweenTurns = 1000;

    private ObservableList<Node> observableList;
    private Scene playgroundScene;
    private Scene menuScene;
    private Stage theStage;
    private Button[] playButtons;
    private Level level;
    private Loop loop = new Loop();
    private int levelNumber = 0;

    private class Loop extends AnimationTimer {
        long prevRun = 0;
        boolean hasEnded = false;

        @Override
        public void handle(long now) {
            final int nano = (int) 1e6;
            if ((now - prevRun > timeBetweenTurns * nano) && !hasEnded) {
                int res = level.runTurn();
                if (res != 0) {
                    String resultText = "";
                    if (res == 1) {
                        resultText = "You have lost!";
                    } else if (res == 2) {
                        incrementLevel();
                        resultText = "You have won!";
                    }
                    Text text = new Text(150, 40, resultText);
                    text.setFill(javafx.scene.paint.Color.RED);
                    text.setScaleX(4);
                    text.setScaleY(4);
                    text.setScaleZ(4);
                    observableList.add(text);
                    hasEnded = true;
                }
                prevRun = now;
            }
        }
    }

    /**
     * Starts the game.
     *
     * @param args Ignored.
     */
    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        observableList = root.getChildren();
        theStage = primaryStage;
        theStage.setTitle("MÁV Vasutas Szimulátor - Az utasok visszatérnek");

        setUpMenuScene();
        setUpPlaygroundScene(root);

        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    private void setUpPlaygroundScene(Group root) {
        playgroundScene = new Scene(root, 800, 800, javafx.scene.paint.Color.BEIGE);
        playgroundScene.addEventFilter(KeyEvent.KEY_PRESSED, Organizer.this::cleanAndStart);
    }

    private void setUpMenuScene() {
        GridPane grid = setUpMenuGrip();
        String[] buttonNames = getButtonNames();
        setUpButtons(grid, buttonNames);
        playButtons[0].setDisable(false);
        menuScene = new Scene(grid, 700, 700, javafx.scene.paint.Color.BEIGE);
    }

    private GridPane setUpMenuGrip() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(30);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text scenetitle = new Text("Choose map!");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 75));
        grid.add(scenetitle, 0, 0, 2, 1);
        return grid;
    }

    private void setUpButtons(GridPane grid, String[] buttonNames) {
        for (int i = 0; i < 3; i++) {
            playButtons[i] = new Button(buttonNames[i]);
            playButtons[i].setOnAction(this::ButtonClicked);
            HBox hb = new HBox(50);
            hb.getChildren().addAll(playButtons[i]);
            playButtons[i].setDisable(true);
            grid.add(hb, 1, i + 1);
        }
    }

    private String[] getButtonNames() {
        playButtons = new Button[4];
        String[] buttonNames = new String[4];
        buttonNames[0] = "easy";
        buttonNames[1] = "medium";
        buttonNames[2] = "hard";
        return buttonNames;
    }

    private void ButtonClicked(ActionEvent e) {
        int k = -1;
        for (int i = 0; i < playButtons.length; i++) {
            if (e.getSource().equals(playButtons[i]))
                k = i;
        }
        newGame(k);
    }

    private void incrementLevel() {
        levelNumber++;
        playButtons[levelNumber].setDisable(false);
    }

    private void newGame(int level) {
        String filename = getLevelsFileName(level);

        theStage.setScene(playgroundScene);
        theStage.setFullScreen(true);
        initializeProgram(filename);
        startNewLoop();
    }

    private void startNewLoop() {
        loop.stop();
        loop = new Loop();
        loop.start();
    }

    private void initializeProgram(String filename) {
        File file = new File(filename);
        level = new Level(observableList, timeBetweenTurns);
        level.initializeMap(file);
    }

    private String getLevelsFileName(int level) {
        String filename;
        switch (level) {
            case 0:
                filename = "easyMap.txt";
                break;
            case 1:
                filename = "mediumMap.txt";
                break;
            case 2:
                filename = "hardMap.txt";
                break;
            default:
                filename = "easyMap.txt";
                break;
        }
        return filename;
    }

    private void cleanAndStart(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            theStage.setScene(menuScene);
            loop.stop();
            return;
        }
        newGame(levelNumber);
    }
}
