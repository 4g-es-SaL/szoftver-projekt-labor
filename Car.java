
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
        this.rail = rail;
        this.prevRail = prevRail;
        this.next = next;
        this.color = color;
        empty = false;
    }

    /**
     * 
     */
    protected Rail rail;
    protected Rail prevRail;
    protected Car next;
    protected Color color;
    protected boolean canEmpty;
    protected boolean empty;

    /**
     * @return
     */
    public int runTurn() {
        System.out.print(rail.id + " ");
        int ret = 1;
        Rail tmp = rail;
        rail = rail.carMoves(this, prevRail);
        prevRail = tmp;
        if(next != null) {
            int r = next.runTurn();
            if(r == 2 && empty)
                ret = 2;
        }
        else{
            if(empty)
                ret = 2;
        }
        return ret;
    }

    /**
     * @param c
     */
    public void atStation(Color c) {
        if(color == c && canEmpty){
            if(next != null)
                next.setCanEmpty(true);
            empty = true;
            System.out.println("urit");
        }
    }

    /**
     * @param b
     */
    public void setCanEmpty(boolean b) {
        canEmpty = b;
    }

}