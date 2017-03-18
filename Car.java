
/**
 * Created by matech on 2017. 02. 20..
 */
public class Car {

    /**
     * Create a new Car object.
     * @param rail The Car will stand on this Rail.
     * @param prevRail The Car has come from this Rail.
     * @param next In the train, the Car behind this Car.
     * @param color The Color of the Car.
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
     * The Car moves to the next Rail in the network. Pulls the Car behind it.
     * @return 1 if there was a collision, 0 otherwise.
     */
    public int runTurn() { MethodPrinter.enterMethod();
//        System.out.print(rail.id + " ");

        int isCollision = move();
        if (isCollision == 1) {
            MethodPrinter.leaveMethod(); return isCollision;
        }

        int res = callNextCar();
        MethodPrinter.leaveMethod(); return res;
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
     * Sets the Cars Color to NO_COLOR, if c equals the Cars Color and canEmpty is true.
     * @param c The Color to compare.
     */
    public void atStation(Color c) { MethodPrinter.enterMethod();
        if(color == c && canEmpty){
            if(next != null)
                next.setCanEmpty(false);
            empty = true;
        }
        MethodPrinter.leaveMethod();
    }

    /**
     * @param b
     */
    public void setCanEmpty(boolean b) { MethodPrinter.enterMethod();
        canEmpty = b;
        if(next != null)
        	next.setCanEmpty(b);
        MethodPrinter.leaveMethod();
    }

}