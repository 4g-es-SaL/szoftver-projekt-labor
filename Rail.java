
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by matech on 2017. 02. 20..
 */
public class Rail {

    /**
     * @param from
     * @param to
     */
    public Rail(Rail from, Rail to) {
        id = idGenerator++;
        this.from = from;
        this.to = to;
    }

    public static int idGenerator = 0;
    protected int id;
    protected Rail from;
    protected Rail to;
    protected Car car;
    protected Color color;

    /**
     * @param t
     * @param prev
     * @return
     */
    public Rail carMoves(Car t, Rail prev) {
        if(from == prev)
            return to;
        else
            return from;
    }

    /**
     * @param r
     * @return
     */
    public void setFrom(Rail r) {
        from = r;
    }

    /**
     * @param r
     * @return
     */
    public void setTo(Rail r) {
        to = r;
    }

}