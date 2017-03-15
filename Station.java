
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
        // TODO implement here
        this.color = color;
    }

    /**
     *
     */
    protected Color color;




}