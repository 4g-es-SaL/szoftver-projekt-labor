
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
        this.from = from;
        this.to = to;
    }

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
        // TODO implement here
        throw new NotImplementedException();
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