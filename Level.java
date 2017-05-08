import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Communicates with the user and the {@link Playground}.
 */
public class Level {
    private final int carSize = 75;
    private final int bias = 0;
    private final int timeBetweenTurns;
    @SuppressWarnings("FieldCanBeLocal")
    private int drugFactor = 1;

    private Playground playground;
    private ObservableList<Node> observableList;

    private Map<LineIdentifier, Line> lines = new HashMap<>();
    private Map<Rail, Coordinates> coordinates = new HashMap<>();
    private Map<Car, Circle> carCircles = new HashMap<>();
    private Map<Station, Rectangle> stationRectangles = new HashMap<>();

    private Polygon mountain = new Polygon();

    public Level(ObservableList<Node> observableList, int timeBetweenTurns) {
        this.timeBetweenTurns = timeBetweenTurns;
        observableList.clear();
        this.observableList = observableList;
        observableList.add(mountain);
        mountain.setFill(javafx.scene.paint.Color.SLATEGRAY);
        mountain.toBack();
    }

    public void initializeMap(File f) {
        playground = new Playground(f, this);
    }

    public int runTurn() {
        return playground.runTurn();
    }

    //region Map drawing
    /**
     * Multiplies the give x and y with carSize and add bias, and converts to a {@link Coordinates}.
     *
     * @param x
     * @param y
     * @return
     */
    private Coordinates transformToLocalCoordinates(float x, float y) {
        return new Coordinates(x * carSize + bias, y * carSize + bias);
    }

    /**
     * Draws a {@link Rail} at the given place. If its neighboors was not previously added, the drawing will happen,
     * when they are added.
     *
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
     *
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
            line.setStrokeWidth(carSize / 15);
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
     * Draws a {@link Station} at the given place. {@link #addRail(Rail, int, int)} muss be called first.
     *
     * @param s
     * @param x
     * @param y
     */
    public void addStation(Station s, int x, int y) {
        Coordinates coords = transformToLocalCoordinates(x, y);
        Rectangle rec = new Rectangle(coords.getX() - carSize / 4, coords.getY() - carSize / 4, carSize / 2, carSize / 2);
        rec.setFill(Level.ColorToJavafx(s.getColor()));
        observableList.add(rec);
        stationRectangles.put(s, rec);
        updateStation(s);
    }

    /**
     * Updates a previously added {@link Station} to its current status.
     *
     * @param s
     */
    public void updateStation(Station s) {
        Rectangle rec = stationRectangles.get(s);
        int arc = 0;
        if (s.isEmpty()) {
            arc = carSize / 2;
        }
        rec.setArcHeight(arc);
        rec.setArcWidth(arc);
    }

    /**
     * Draws a {@link CrossRail} at the given place.
     *
     * @param cross
     * @param x
     * @param y
     */
    public void addCrossRail(CrossRail cross, int x, int y) {
        addRail(cross, x, y);
        extendRailToCrossRail(cross);
    }

    /**
     * Extends the graphic of a previously added {@link Rail}s to a {@link CrossRail}s.
     *
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
     *
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
     *
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
     *
     * @param sw
     */
    private void drawSwitchCircle(Switch sw) {
        Coordinates coords = coordinates.get(sw);
        Circle cir = new Circle(coords.getX(), coords.getY(), carSize / 4);
        cir.setFill(javafx.scene.paint.Color.BLACK);
        cir.setOnMouseClicked(event -> changeSwitch(sw));
        observableList.add(cir);
    }

    /**
     * Changes the {@link Switch} and draws the result. It is called by a click.
     *
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
     *
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
     *
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
     *
     * @param rail
     */
    public void extendRailToTunnelEntryPoint(Rail rail) {
        Coordinates coords = this.coordinates.get(rail);
        Circle rec = new Circle(coords.getX(), coords.getY(), carSize / 4);
        rec.setFill(javafx.scene.paint.Color.BROWN);
        rec.setOnMouseClicked(event -> {
            Tunnel tunnel = playground.buildTunnelEnd(rail);
            drawTunnelsLine(tunnel);
        });
        observableList.add(rec);
    }

    /**
     * Draws a clickable {@link Line} for the {@link Tunnel}, if possible. If the {@link Line} is clicked, the tunnel is destroyed.
     *
     * @param tunnel
     */
    private void drawTunnelsLine(Tunnel tunnel) {
        Rail from = tunnel.getFrom();
        Rail to = tunnel.getTo();
        if (from != null && to != null) {
            Line line = drawLineBetweenRails(from, to);
            line.setStrokeWidth(carSize);
            line.setStroke(javafx.scene.paint.Color.BROWN);
            line.toFront();
            line.setOnMouseClicked(event1 -> destroyTunnel(tunnel, line));
        }
    }

    /**
     * Destroys the {@link Tunnel} and the {@link Line} related to it, if possible.
     *
     * @param tunnel
     * @param line
     */
    private void destroyTunnel(Tunnel tunnel, Line line) {
        lines.remove(new LineIdentifier(tunnel.from, tunnel.to));
        if (tunnel.destroyTunnel()) {
            observableList.remove(line);
        }
    }

    public void addMountainPoint(float x, float y) {
        Coordinates coords = transformToLocalCoordinates(x, y);
        mountain.getPoints().add((double) coords.getX());
        mountain.getPoints().add((double) coords.getY());
    }

    /**
     * Draws a circle, whit the {@link Car}s color.
     *
     * @param car
     */
    public void addCar(Car car) {
        Circle circle = new Circle(0);
        carCircles.put(car, circle);
        observableList.add(circle);
        updateCar(car);
    }

    /**
     * Updates a previously added {@link Car} to its current status.
     *
     * @param car
     */
    public void updateCar(Car car) {
        Circle circle = carCircles.get(car);
        animateCarPosition(car, timeBetweenTurns * drugFactor);
        if (car.hasSpawn()) {
            circle.setRadius(carSize / 2);
        }

        javafx.scene.paint.Color color = Level.ColorToJavafx(car.getColor());
        if (car.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                color = color.darker();
            }
        }
        circle.setFill(color);
    }

    /**
     * Moves the graphic of the {@link Car} to its new position, in the given duration.
     *
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
        translateTransition.setInterpolator(Interpolator.LINEAR);
        translateTransition.play();
    }

    private void setCarPosition(Coordinates coords, Circle circle) {
        circle.setCenterX(coords.getX());
        circle.setCenterY(coords.getY()); //Does NOT works dont know why.
    }

    private static javafx.scene.paint.Color ColorToJavafx(Color c) {
        switch (c) {
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
    //endregion


}