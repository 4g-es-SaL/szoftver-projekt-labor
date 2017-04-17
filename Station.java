import java.util.ArrayList;

/**
 * Represents a station object. A station is a special type of {@link Rail}. It can have a {@link Color}, and can empty {@link Car}s.
 */
public class Station extends Rail {

    protected Color color;
    protected ArrayList<Color> passColors;

    /**
     * Create a new {@link Station} instance.
     * @param from Connected to this {@link Rail}.
     * @param to Connected to this {@link Rail}. If Tunnel will build this neighbor will be replaced.
     * @param color The {@link Color} of the {@link Station}.
     */
    public Station(Rail from, Rail to, Color color) {
        super(from, to);
        this.color = color;
        passColors = new ArrayList<>();
    }

    @Override
    protected void addCar(Car c) throws Exception {
        super.addCar(c);
        c.atStation(this);
    }

    /**
     * Gives back the colour of the station.
     * @return The colour({@link Color}) of the station.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Adds passengers given colour to the station.
     * @param c The colour({@link Color}) of the passengers to be added.
     */
    public void addPassanger(Color c) {
        passColors.add(c);
    }

    /**
     * Removes the passengers given colour from the station.
     * @param c The colour({@link Color}) of the passengers to be removed.
     * @return True if there were passengers given colour and they were successfully removed, false otherwise.
     */
    public boolean removePassenger(Color c) {
        return passColors.remove(c);
    }

    /**
     * Gives back whether the station is empty.
     * @return True if no passengers are present on the station, false otherwise.
     */
    public boolean isEmpty() {
        return passColors.isEmpty();
    }

    @Override
    public String toString() {
        return "Station{" +
                super.toString() +
                ", color=" + color +
                ", passColors=" + passColors + "}";
    }
}