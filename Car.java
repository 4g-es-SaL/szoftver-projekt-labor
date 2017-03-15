
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
     * The Car moves to the next Rail in the network.
     * @return 1 if there was a collision, 0 otherwise.
     */
    public int runTurn() {
        System.out.print(rail.id + " ");

        int isCollision = move();
        if (isCollision == 1) {
            return isCollision;
        }

        return callNextCar();
    }

    private int move() {
        Rail tmp = rail;
        try {
            rail = rail.carMoves(this, prevRail);
        } catch (Exception e) {
            return 1;
        }
        prevRail = tmp;
        return 0;
    }

    private int callNextCar() {
        if(next != null) {
            return next.runTurn();
        }
        return 0;
    }

    /**
     * @param c
     */
    public void atStation(Color c) {
        if(color == c && canEmpty){
            if(next != null)
                next.setCanEmpty(true);
            empty = true;
            System.out.print("urit ");
        }
    }

    /**
     * @param b
     */
    public void setCanEmpty(boolean b) {
        canEmpty = b;
    }

}