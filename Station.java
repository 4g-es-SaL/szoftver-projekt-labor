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

    public Color getColor() {
        return color;
    }

    public void addPassanger(Color c) {
        passColors.add(c);
    }

    public boolean removePassenger(Color c) {
        return passColors.remove(c);
    }

    @Override
    public String toString() {
        return "Station{" +
                super.toString() +
                ", color=" + color +
                ", passColors=" + passColors + "}";
    }
}