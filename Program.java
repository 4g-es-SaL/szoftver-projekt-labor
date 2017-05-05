import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Communicates with the user and the {@link Playground}.
 */
public class Program extends Application {
    final int size = 75;
    final int bias = 75;

    protected static Playground playground;
    ObservableList<Node> observableList;

    protected static Map<Rail, Line> rectangles = new HashMap<>();
    protected static Map<Rail, Coordinates> coordinates = new HashMap<>();
    protected static Map< Rectangle, Switch> switches = new HashMap<>();
    protected static Map<Rectangle, Rail> tunnelEnds = new HashMap<>();

    /**
     * Starts the program.
     * @param args Ignored.
     */
    public static void main(String[] args) {
        launch(args);
        //run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        observableList = root.getChildren();

//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setInitialFileName("D:\\projektek\\szoftver-projekt-labor\\src");
//        final Button openButton = new Button("Open a Picture...");
//        openButton.setAlignment(Pos.CENTER);
//        openButton.setOnAction(e -> {
//                    File file = fileChooser.showOpenDialog(primaryStage);
//                    if (file != null) {
//                        playground = new Playground(file, Program.this);
//                    }
//                    observableList.remove(openButton);
//                });
//        observableList.add(openButton);
        File file = new File("D:\\projektek\\szoftver-projekt-labor\\src\\kiurules.txt");
        playground = new Playground(file, Program.this);

        Scene s = new Scene(root, 600,600);
        primaryStage.setScene(s);
        primaryStage.show();
    }

    //region text based
    /**
     * Game loop.
     */
    protected static void run() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            String userInput = scanner.nextLine();
            String[] userInputSplit = userInput.split(" ");
            try {
                switch (userInputSplit[0]) {
                    case "init":
                        init(userInputSplit[1]);
                        break;
                    case "switch":
                        playground.changeSwitch(Integer.parseInt(userInputSplit[1]));
                        break;
                    case "build": 
                    	playground.buildNewTunnel(Integer.parseInt(userInputSplit[1]), Integer.parseInt(userInputSplit[2]));
                    	break;
                    case "destroy":
                    	playground.destroyTunnelEnd();
                        break;
                    case "passenger":
                        playground.addPassenger(Integer.parseInt(userInputSplit[1]), Integer.parseInt(userInputSplit[2]));
                        break;
                    case "move":
                        int ires = playground.runTurn();
                        String res = getRunResultText(ires);
                        printTextIfNotEmpty(res);
                        break;
                    case "map":
                        playground.printMap();
                        break;
                    case "exit":
                        exit = true;
                        break;
                    default:
                        printHelp();
                }
            } catch (Exception e) {
                printHelp();
            }
        }
    }

    /**
     * If you can't figure out by the name, you are not worthy to be called 'programmer'.
     */
    private static void printTextIfNotEmpty(String res) {
        if (!res.equals("")) {
            System.out.println(res);
        }
    }

    /**
     * If you can't figure out by the name, you are not worthy to be called 'programmer'.
     */
    private static String getRunResultText(int ires) {
        String res = "";
        switch (ires) {
            case 1:
                res = "Collision!";
                break;
            case 2:
                res = "GAME OVER";
                break;
            default:
                break;
        }
        return res;
    }

    protected static void init(String s) {
        File f = new File(s);
        playground = new Playground(f, null);
    }
    //endregion

    private Coordinates transformToLocalCoordinates(int x, int y) {
        return new Coordinates(x*size + bias, y*size + bias);
    }

    /**
     * @param rail
     * @param x
     * @param y
     */
    public void addRail(Rail rail, int x, int y) {
        // TODO implement here
        Coordinates railCoords = transformToLocalCoordinates(x, y);
        Program.coordinates.put(rail, railCoords);

        Rail from = rail.getFrom();
        drawLineFromRailToPoint(from, railCoords);
        Rail to = rail.getTo();
        drawLineFromRailToPoint(to, railCoords);
    }

    /**
     * @param s
     * @param x
     * @param y
     */
    public void addStation(Station s, int x, int y) {
        Coordinates coords = transformToLocalCoordinates(x, y);
        Rectangle rec = new Rectangle(coords.getX()-size/4, coords.getY()-size/4, size/2, size/2);
        rec.setFill(javafx.scene.paint.Color.RED);
        observableList.add(rec);
    }

    public void addCrossRail(CrossRail cross, int x, int y) {
        addRail(cross, x ,y);
        Coordinates crossCoords = transformToLocalCoordinates(x, y);

        Rail from2 = cross.getFrom2();
        drawLineFromRailToPoint(from2, crossCoords);
        Rail to2 = cross.getTo2();
        drawLineFromRailToPoint(to2, crossCoords);
    }

    private void drawLineFromRailToPoint(Rail rail, Coordinates coords) {
        Coordinates fCoord = coordinates.get(rail);
        if (fCoord != null) {
            Line fLine = new Line(coords.getX(), coords.getY(), fCoord.getX(), fCoord.getY());
            observableList.add(fLine);
        }
    }

    /**
     * @param r
     * @param x
     * @param y
     */
    public static void addMountainEntryPoint(Rail r, int x, int y) {
        // TODO implement here
    }

    /**
     * @param x
     * @param y
     */
    public static void addMountain(int x, int y) {
        // TODO implement here
    }

    /**
     * @param r
     */
    public static void updateRail(Rail r) {
        // TODO implement here
    }

    protected static void printHelp() {
        System.out.println("Your input is not recognizable as an existing command!");
        System.out.println("init <file>\n" +
                "Leírás: beolvassa a megadott file-t és ez alapján felépíti a játékterepet\n" +
                "Opciók: file: a beolvasandó file\n" +
                "\n" +
                "map\n"+
                "Leírás: Kiírja az összes sínt, alagutat és váltót\n"+
                "\n" +
                "switch <switch id>\n" +
                "Leírás: A <switch id>-val azonosított Switch objektum állását átállítja\n" +
                "Opciók: switch id: az állítandó Switch azonosítója\n" +
                "\n" +
                "build <rail id1> <rail id2>\n" +
                "Leírás: A <rail id1>-val, illetve <rail id2>-vel azonosított Rail objektumok között felépít egy alagutat, ha lehetséges.\n" +
                "Opciók: <rail id1> <rail id2>: A Rail objektumok azonosítói\n" +
                "\n" +
                "destroy <rail id1> <rail id2>\n" +
                "Leírás: A <rail id1>-val, illetve <rail id2>-vel azonosított Rail objektumok között felépített alagutat lebontja, ha az létezik.\n" +
                "Opciók: <rail id1> <rail id2>: A Rail objektumok azonosítói\n" +
                "\n" +
                "passenger <color> <rail id>\n" +
                "Leírás: A <rail id>-val azonosított állomáshoz(ha az állomás) hozzáad color színű utasokat\n" +
                "Opciók: <color>: a felszállítandó utasok színe\n" +
                "\t\t  <rail id>: az állomás azonosítója\n" +
                "\n" +
                "move\n" +
                "Leírás: Egy kör lejátszása\n" +
                "\n" +
                "exit\n" +
                "Leírás: Kilépés");
    }

}