
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

    public Rail carMoves(Car t, Rail prev) {
        t.atStation(color);
        if(from == prev)
            return to;
        else
            return from;
    }

    protected Color color;




}