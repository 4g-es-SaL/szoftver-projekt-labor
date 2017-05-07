import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Communicates with the user and the {@link Playground}.
 */
public class Program extends Application {
    final int size = 75;
    final int bias = 0;

    protected Playground playground;
    ObservableList<Node> observableList;

    protected Map<LineIdentifier, Line> lines = new HashMap<>();
    protected Map<Rail, Coordinates> coordinates = new HashMap<>();
    protected Map<Car, Circle> carCircles = new HashMap<>();
    protected Map<Station, Rectangle> stationRectangles = new HashMap<>();

    protected Polygon  mountain = new Polygon();
    protected boolean isMountainInitialized = false;

    /**
     * Starts the program.
     * @param args Ignored.
     */
    public static void main(String[] args) {
        try {
            launch(args);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        observableList = root.getChildren();

        Scene s = new Scene(root, 800,800, javafx.scene.paint.Color.LIGHTGREEN);
        primaryStage.setScene(s);
        observableList.add(mountain);
        mountain.setFill(javafx.scene.paint.Color.SLATEGRAY);
        mountain.toBack();

        AnimationTimer loop = new AnimationTimer() {
            long prevRun = 0;
            boolean hasEnded = false;
            @Override
            public void handle(long now) {
                if ((now - prevRun > 1e9) && !hasEnded) {
                    int res = playground.runTurn();
                    if (res != 0) {
                        if (res == 1) {
                            System.out.println("You have lost!");
                            Text text = new Text(150, 40, "You have lost!");
                            text.setFill(javafx.scene.paint.Color.RED);
                            text.setScaleX(4);
                            text.setScaleY(4);
                            text.setScaleZ(4);
                            observableList.add(text);
                        } else if (res == 2) {
                            System.out.println("You have won!");
                            Text text = new Text(150, 40, "You have won!");
                            text.setFill(javafx.scene.paint.Color.RED);
                            text.setScaleX(4);
                            text.setScaleY(4);
                            text.setScaleZ(4);
                            observableList.add(text);
                        }
                        s.addEventFilter(KeyEvent.KEY_PRESSED, event -> Platform.exit());
                        hasEnded = true;
                    }
                    prevRun = now;
                }
            }
        };

        FileChooser fileChooser = new FileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setInitialDirectory(workingDirectory);
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            playground = new Playground(file, Program.this);
            loop.start();
        }

//        File file = new File("mountain.txt");
//        playground = new Playground(file, Program.this);
//        loop.start();



        //primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    //region text based
    /**
     * Game loop.
     */
    protected void run() {
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

    protected void init(String s) {
        File f = new File(s);
        playground = new Playground(f, this);
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
    //endregion

    /**
     * Multiplies the give x and y with size and add bias, and converts to a {@link Coordinates}.
     * @param x
     * @param y
     * @return
     */
    private Coordinates transformToLocalCoordinates(float x, float y) {
        return new Coordinates(x*size + bias, y*size + bias);
    }

    /**Draws a {@link Rail} at the given place. If its neighboors was not previously added, the drawing will happen,
     * when they are added.
     * @param rail
     * @param x
     * @param y
     */
    public void addRail(Rail rail, int x, int y) {
        Coordinates railCoords = transformToLocalCoordinates(x, y);
        coordinates.put(rail, railCoords);

        Rail from = rail.getFrom();
        drawLineBetweenRails(from, rail);
        Rail to = rail.getTo();
        drawLineBetweenRails(to, rail);
    }

    /**
     * Draws a {@link Line} between the given {@link Rail}s.
     * @param a
     * @param b
     * @return the created {@link Line}.
     */
    private Line drawLineBetweenRails(Rail a, Rail b) {
        Line line = lines.get(new LineIdentifier(a, b));
        if (line != null) return line;

        Coordinates aCoord = coordinates.get(a);
        Coordinates bCoord = coordinates.get(b);

        if (aCoord != null && bCoord != null) {
            line = new Line(aCoord.getX(), aCoord.getY(), bCoord.getX(), bCoord.getY());
            line.setStrokeWidth(size/15);
            line.setStrokeLineCap(StrokeLineCap.ROUND);
            observableList.add(line);
            line.toBack();

            LineIdentifier li = new LineIdentifier(a, b);
            lines.put(li, line);
            return line;
        }
        return null;
    }

    /**Draws a {@link Station} at the given place. {@link #addRail(Rail, int, int)} muss be called first.
     * @param s
     * @param x
     * @param y
     */
    public void addStation(Station s, int x, int y) {
        Coordinates coords = transformToLocalCoordinates(x, y);
        Rectangle rec = new Rectangle(coords.getX()-size/4, coords.getY()-size/4, size/2, size/2);
        rec.setFill(Program.ColorToJavafx(s.getColor()));
        observableList.add(rec);
        stationRectangles.put(s, rec);
        updateStation(s);
    }

    public void updateStation(Station s) {
        Rectangle rec = stationRectangles.get(s);
        int arc = 0;
        if (s.isEmpty()) {
            arc = size / 2;
        }
        rec.setArcHeight(arc);
        rec.setArcWidth(arc);
    }

    /**
     * Draws a {@link CrossRail} at the given place.
     * @param cross
     * @param x
     * @param y
     */
    public void addCrossRail(CrossRail cross, int x, int y) {
        addRail(cross, x ,y);
        extendRailToCrossRail(cross);
    }

    /**
     * Extends the graphic of a previously added {@link Rail}s to a {@link CrossRail}s.
     * @param cross
     */
    public void extendRailToCrossRail(CrossRail cross) {
        Coordinates crossCoords = coordinates.get(cross);

        Rail from2 = cross.getFrom2();
        drawLineBetweenRails(from2, cross);
        Rail to2 = cross.getTo2();
        drawLineBetweenRails(to2, cross);
    }

    /**
     * Draws a {@link Switch} at the given place.
     * @param sw
     * @param x
     * @param y
     */
    public void addSwitch(Switch sw, int x, int y) {
        addRail(sw, x, y);
        extendRailToSwitch(sw);
    }

    /**
     * Extends the graphic of a previously added {@link Rail}s to a {@link Switch}s.
     * @param sw
     */
    public void extendRailToSwitch(Switch sw) {
        ArrayList<Rail> tos = sw.getTos();
        for (Rail r : tos) {
            Line line = drawLineBetweenRails(r, sw);
            if (line != null) {
                line.setStroke(javafx.scene.paint.Color.GRAY);
            }
        }
        Rail t = sw.getTo();
        changeLineColor(sw, t, javafx.scene.paint.Color.BLACK);
        Rail from = sw.getFrom();
        changeLineColor(sw, from, javafx.scene.paint.Color.BLUE);
        drawSwitchCircle(sw);
    }

    /**
     * Draws the Circle, which can be clicked, to change the {@link Switch}.
     * @param sw
     */
    private void drawSwitchCircle(Switch sw) {
        Coordinates coords = coordinates.get(sw);
        Circle cir = new Circle(coords.getX(), coords.getY(), size/4);
        cir.setFill(javafx.scene.paint.Color.BLACK);
        cir.setOnMouseClicked(event -> changeSwitch(sw));
        observableList.add(cir);
    }

    /**
     * Changes the {@link Switch} and draws the result. It is called by a click.
     * @param sw
     */
    private void changeSwitch(Switch sw) {
        Rail to = sw.getTo();
        changeLineColor(sw, to, javafx.scene.paint.Color.GRAY);
        sw.changeDir();
        to = sw.getTo();
        changeLineColor(sw, to, javafx.scene.paint.Color.BLACK);
    }

    /**
     * Changes rhe {@link Line}s color, which is between the {@link Rail}s.
     * @param a
     * @param b
     * @param color
     */
    private void changeLineColor(Rail a, Rail b, javafx.scene.paint.Color color) {
        Line line = lines.get(new LineIdentifier(a, b));
        line.setStroke(color);
    }

    /**
     * Draws a {@link Rail} and a clickable mountain entrypoint at the given place.
     * @param rail
     * @param x
     * @param y
     */
    public void addTunnelEntryPoint(Rail rail, int x, int y) {
        addRail(rail, x, y);
        extendRailToTunnelEntryPoint(rail);
    }

    /**
     * Draws a clickable mountain entrypoint at the top of the given {@link Rail}.
     * @param rail
     */
    public void extendRailToTunnelEntryPoint(Rail rail) {
        Coordinates coords = this.coordinates.get(rail);
        Circle rec = new Circle(coords.getX(), coords.getY(), size/4);
        rec.setFill(javafx.scene.paint.Color.BROWN);
        rec.setOnMouseClicked(event -> {
            Tunnel tunnel = playground.buildTunnelEnd(rail);
            drawTunnelsLine(tunnel);
        });
        observableList.add(rec);
    }

    public void addMountainPoint(float x, float y) {
        Coordinates coords = transformToLocalCoordinates(x, y);
        mountain.getPoints().add((double) coords.getX());
        mountain.getPoints().add((double) coords.getY());
    }

    /**
     * Draws a clickable {@link Line} for the {@link Tunnel}, if possible. If the {@link Line} is clicked, the tunnel is destroyed.
     * @param tunnel
     */
    private void drawTunnelsLine(Tunnel tunnel) {
        Rail from = tunnel.getFrom();
        Rail to = tunnel.getTo();
        if (from != null && to != null) {
            Line line = drawLineBetweenRails(from, to);
            line.setStrokeWidth(size);
            line.setStroke(javafx.scene.paint.Color.BROWN);
            line.toFront();
            line.setOnMouseClicked(event1 -> destroyTunnel(tunnel, line));
        }
    }

    /**
     * Destroys the {@link Tunnel} and the {@link Line} related to it, if possible.
     * @param tunnel
     * @param line
     */
    private void destroyTunnel(Tunnel tunnel, Line line) {
        lines.remove(new LineIdentifier(tunnel.from, tunnel.to));
        if (tunnel.destroyTunnel()) {
            observableList.remove(line);
        }
    }

    /**
     * Draws a circle, whit the {@link Car}s color.
     * @param car
     */
    public void addCar(Car car) {
        Circle circle = new Circle(0);
        carCircles.put(car, circle);
        observableList.add(circle);
        updateCar(car);
    }

    /**
     * Updates a previously added {@link Car} to its current station.
     * @param car
     */
    public void updateCar(Car car) {
        Circle circle = carCircles.get(car);
        animateCarPosition(car, 900);
        if (car.hasSpawn()) {
            circle.setRadius(size / 2);
        }

        javafx.scene.paint.Color color = Program.ColorToJavafx(car.getColor());
        if (car.isEmpty()) {
            for(int i=0;i<3;i++){
                color = color.darker();
            }
        }
        circle.setFill(color);
    }

    /**
     * Moves the graphic of the {@link Car} to its new position, in the given duration.
     * @param car
     * @param duration
     */
    private void animateCarPosition(Car car, double duration) {
        Rail rail = car.getCurrentRail();
        Coordinates coords = this.coordinates.get(rail);
        if (coords == null) return;
        Circle circle = carCircles.get(car);
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(duration), circle);
        translateTransition.setToX(coords.getX());
        translateTransition.setToY(coords.getY());
        translateTransition.play();
    }

    private void setCarPosition(Coordinates coords, Circle circle) {
        circle.setCenterX(coords.getX());
        circle.setCenterY(coords.getY()); //Does NOT works dont know why.
    }

    private static javafx.scene.paint.Color ColorToJavafx(Color c) {
        switch (c){
            case RED:
                return javafx.scene.paint.Color.RED;
            case GREEN:
                return javafx.scene.paint.Color.GREEN;
            case BLUE:
                return javafx.scene.paint.Color.BLUE;
            case YELLOW:
                return javafx.scene.paint.Color.YELLOW;
            case PURPLE:
                return javafx.scene.paint.Color.PURPLE;
            case NO_COLOR:
                return javafx.scene.paint.Color.BLACK;
            default:
                return javafx.scene.paint.Color.BLACK;
        }
    }
}