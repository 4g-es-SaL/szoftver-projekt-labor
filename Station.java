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

    /**
     * Same as {@link Rail#carMoves(Car, Rail)}, but calls t.{@link Car#atStation(Color)}.
     * @param c The {@link Car} that moves.
     * @param prev The {@link Rail} where the {@link Car} came from.
     * @return The {@link Rail} where c stands.
     * @throws Exception In occasion of collision!
     */
    @Override
    public Rail carMoves(Car c, Rail prev) throws Exception { MethodPrinter.enterMethod();
        try {
            Rail res = super.carMoves(c, prev);
            c.atStation(color);
            MethodPrinter.leaveMethod(); return res;
        } catch(Exception e) {
            MethodPrinter.leaveMethod();
            throw e;
        }
    }

    public void addPassanger(Color c) {
        passColors.add(c);
    }

    @Override
    public String toString() {
        return "Station{" +
                super.toString() +
                ", color=" + color +
                ", passColors=" + passColors + "}";
    }
}