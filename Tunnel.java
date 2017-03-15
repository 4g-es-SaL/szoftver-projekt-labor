
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by matech on 2017. 02. 20..
 */
public class Tunnel extends Rail {

    protected Rail fromsNeighboor;
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
        if(from == null)
            from = r;
        else if(to == null)
            to = r;
    }

    /**
     *
     */
    public void destroyTunnel() {
        from = null;
        to = null;
    }

}