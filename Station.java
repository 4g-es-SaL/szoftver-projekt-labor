
/**
 *
 */
public class Station extends Rail {

    /**
     * @param from
     * @param to
     * @param color
     */
    public Station(Rail from, Rail to, Color color) {
        super(from, to);
        this.color = color;
    }

    public Rail carMoves(Car t, Rail prev) throws Exception { MethodPrinter.enterMethod();
        try {
            Rail res = super.carMoves(t, prev);
            t.atStation(color);
            MethodPrinter.leaveMethod(); return res;
        } catch(Exception e) {
            MethodPrinter.leaveMethod();
            throw e;
        }
    }

    protected Color color;

    @Override
    public String toString() {
        return "Station{" +
                super.toString() +
                "color=" + color +
                "}";
    }
}