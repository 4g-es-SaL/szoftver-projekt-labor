
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by matech on 2017. 02. 20..
 */
public class Tunnel extends Rail {

    /**
     *
     */
    protected Rail fromsNeighboor;

    /**
     *
     */
    protected Rail tosNeighboor;


    /**
     * @param from
     * @param to
     */
    public Tunnel(Rail from, Rail to) {
        super(from, to);
    }

    /**
     * @param r
     */
    public void buildTunnel(Rail r) {
        // TODO implement here
        throw new NotImplementedException();
    }

    /**
     *
     */
    public void destroyTunnel() {
        // TODO implement here
        throw new NotImplementedException();
    }

}