
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by matech on 2017. 02. 20..
 */
public class Car {

    /**
     *
     * @param rail
     * @param prevRail
     * @param next
     * @param color
     */
    public Car(Rail rail, Rail prevRail, Car next, Color color) {
        // TODO implement here
        throw new NotImplementedException();
    }

    /**
     * 
     */
    protected Rail rail;

    /**
     * 
     */
    protected Rail prevRail;

    /**
     * 
     */
    protected Car next;

    /**
     * 
     */
    protected Color color;

    /**
     * 
     */
    protected boolean canEmpty;

    /**
     * @return
     */
    public int runTurn() {
        // TODO implement here
        throw new NotImplementedException();
    }

    /**
     * @param c
     */
    public void atStation(Color c) {
        // TODO implement here
        throw new NotImplementedException();
    }

    /**
     * @param b
     */
    public void setCanEmpty(boolean b) {
        // TODO implement here
        throw new NotImplementedException();
    }

}