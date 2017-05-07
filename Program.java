import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
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
    protected Map<Rectangle, Rail> mountainPoints = new HashMap<>();

    Line draggedLine;

    /**
     * Starts the program.
     * @param args Ignored.
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        observableList = root.getChildren();

        AnimationTimer loop = new AnimationTimer() {
            long prevRun = 0;
            @Override
            public void handle(long now) {
                if (now - prevRun > 1e9) {
                    int res = playground.runTurn();
                    if (res != 0) {
                        if (res == 1) {
                            System.out.println("You have lost!");
                        } else if (res == 2) {
                            System.out.println("You have won!");
                        }
                        //Platform.exit();
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

        Scene s = new Scene(root, 600,600);
        primaryStage.setScene(s);

        primaryStage.show();


//        int res = 0;
//        do {
//            res = playground.runTurn();
//        } while(res == 0);
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
        coordinates.put(rail, railCoords);

        Rail from = rail.getFrom();
        drawLineBetweenRails(from, rail);
        Rail to = rail.getTo();
        drawLineBetweenRails(to, rail);
    }

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

    /**
     * @param s
     * @param x
     * @param y
     */
    public void addStation(Station s, int x, int y) {
        Coordinates coords = transformToLocalCoordinates(x, y);
        Rectangle rec = new Rectangle(coords.getX()-size/4, coords.getY()-size/4, size/2, size/2);
        rec.setFill(Program.ColorToJavafx(s.getColor()));
        observableList.add(rec);
    }

    public void addCrossRail(CrossRail cross, int x, int y) {
        addRail(cross, x ,y);
        extendRailToCrossRail(cross);
    }

    public void extendRailToCrossRail(CrossRail cross) {
        Coordinates crossCoords = coordinates.get(cross);

        Rail from2 = cross.getFrom2();
        drawLineBetweenRails(from2, cross);
        Rail to2 = cross.getTo2();
        drawLineBetweenRails(to2, cross);

    }

    public void addSwitch(Switch sw, int x, int y) {
        addRail(sw, x, y);
        extendRailToSwitch(sw);
    }

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

    private void drawSwitchCircle(Switch sw) {
        Coordinates coords = coordinates.get(sw);
        Circle cir = new Circle(coords.getX(), coords.getY(), size/4);
        cir.setFill(javafx.scene.paint.Color.BLACK);
        cir.setOnMouseClicked(event -> {
            Rail to = sw.getTo();
            changeLineColor(sw, to, javafx.scene.paint.Color.GRAY);
            sw.changeDir();
            to = sw.getTo();
            changeLineColor(sw, to, javafx.scene.paint.Color.BLACK);
        });
        observableList.add(cir);
    }

    private void changeLineColor(Rail a, Rail b, javafx.scene.paint.Color color) {
        Line line = lines.get(new LineIdentifier(a, b));
        line.setStroke(color);
    }

    /**
     * @param rail
     * @param x
     * @param y
     */
    public void addMountainEntryPoint(Rail rail, int x, int y) {
        // TODO implement here
        addRail(rail, x, y);
        extendRailToMountainEntryPoint(rail);
    }

    public void extendRailToMountainEntryPoint(Rail rail) {
        Coordinates coords = this.coordinates.get(rail);
        Rectangle rec = new Rectangle(coords.getX()-size/4, coords.getY()-size/4, size/2, size/2);
        rec.setFill(javafx.scene.paint.Color.BROWN);
        rec.setOnMouseClicked(new EventHandler<MouseEvent>() {
            private Tunnel prevTunnel;
            private Line prevLine;
            @Override
            public void handle(MouseEvent event) {
                Tunnel tunnel = playground.buildTunnelEnd(rail);
                if (prevLine != null) {
                    observableList.remove(prevLine);
                }
                Rail from = tunnel.getFrom();
                Rail to = tunnel.getTo();
                if (from != null && to != null) {
                    Line line = drawLineBetweenRails(from, to);
                    line.setStrokeWidth(size);
                    line.setStroke(javafx.scene.paint.Color.BROWN);
                    line.toFront();
                    line.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            lines.remove(new LineIdentifier(tunnel.from, tunnel.to));
                            if (tunnel.destroyTunnel()) {
                                observableList.remove(line);
                            }
                        }
                    });
                }
            }
        });
//
//        rec.setOnMousePressed(new EventHandler <MouseEvent>()
//        {
//            public void handle(MouseEvent event)
//            {
//                rec.setMouseTransparent(true);
//                event.setDragDetect(true);
//            }
//        });
//
//        rec.setOnDragDetected(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                draggedLine = new Line(coords.getX(), coords.getY(), coords.getX(), coords.getY());
//                observableList.add(draggedLine);
//                rec.setFill(javafx.scene.paint.Color.BROWN);
//            }
//        });
//
//        rec.setOnMouseDragged(new EventHandler <MouseEvent>()
//        {
//            public void handle(MouseEvent event)
//            {
//                event.setDragDetect(false);
//                double x = event.getX();
//                double y = event.getY();
//                draggedLine.setEndX(x);
//                draggedLine.setEndY(y);
//            }
//        });
//
//        rec.setOnMouseReleased(new EventHandler <MouseEvent>()
//        {
//            public void handle(MouseEvent event)
//            {
//                rec.setMouseTransparent(false);
//                Rectangle a = (Rectangle)event.getSource();
//                Rectangle b = (Rectangle)event.getTarget();
//                a.setFill(javafx.scene.paint.Color.BLACK);
//                b.setFill(javafx.scene.paint.Color.BLACK);
//                observableList.remove(draggedLine);
//            }
//        });
//
//        rec.setOnMouseReleased(new EventHandler<MouseEvent>() {
//            public void handle(MouseEvent event)
//            {
//                Rectangle a = (Rectangle)event.getSource();
//                Rectangle b = (Rectangle)event.getTarget();
//                a.setFill(javafx.scene.paint.Color.BLACK);
//                b.setFill(javafx.scene.paint.Color.BLACK);
//            }
//        });

//        Rectangle a = (Rectangle)event.getSource();
//        Rectangle b = (Rectangle)event.getTarget();
        mountainPoints.put(rec, rail);
        observableList.add(rec);
    }

    /**
     * @param x
     * @param y
     */
    public static void addMountain(int x, int y) {
        // TODO implement here
    }

    public void addCar(Car car) {
        Circle circle = new Circle(size/2);
        carCircles.put(car, circle);
        observableList.add(circle);
        updateCar(car);
    }

    /**
     * @param car
     */
    public void updateCar(Car car) {
        // TODO implement here
        Circle circle = carCircles.get(car);
        animateCarPosition(car, 900);

        javafx.scene.paint.Color color = Program.ColorToJavafx(car.getColor());
        if (car.isEmpty()) {
            color = color.brighter();
        }
        circle.setFill(color);
    }

    private void animateCarPosition(Car car, double duration) {
        Rail rail = car.getCurrentRail();
        Coordinates coords = this.coordinates.get(rail);
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