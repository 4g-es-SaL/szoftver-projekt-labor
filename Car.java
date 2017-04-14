
/**
 * Represents a {@link Car}. A {@link Car} can move on {@link Rail}s.
 */
public class Car {

    protected Rail rail;
    protected Rail prevRail;
    protected Car next;
    protected final Color color;
    protected boolean empty;
    protected Station stationWhereEmpty;
    private boolean canEmpty;

    /**
     * Create a new {@link Car} object. And notifies the {@link Rail}s about its presence.
     * @param rail The {@link Car} will stand on this Rail.
     * @param prevRail The {@link Car} has come from this Rail.
     * @param next In the train, the {@link Car} behind this {@link Car}.
     * @param color The {@link Color} of the {@link Car}.
     */
    public Car(Rail rail, Rail prevRail, Car next, Color color) throws Exception {
        this.rail = rail;
        this.prevRail = prevRail;
        this.next = next;
        this.color = color;
        if (color == Color.NO_COLOR) {
            empty = true;
        }
        canEmpty = false;
        signUpCarOnRail(rail, prevRail);
    }

    //region Constructor privates
    private void signUpCarOnRail(Rail rail, Rail prevRail) throws Exception {
        Rail prevFrom = prevRail.getFrom();
        Rail prevTo = prevRail.getTo();
        if (rail == prevFrom) {
            prevRail.carMoves(this, prevTo);
        } else {
            prevRail.carMoves(this, prevFrom);
        }
    }
    //endregion

    /**
     * The {@link Car} moves to the next {@link Rail} in the network. Pulls the {@link Car} behind it.
     * @return 1 if there was a collision, 0 otherwise.
     */
    public int runTurn() {
        int isCollision = move();
        if (isCollision == 1) {
            return isCollision;
        }

        return callNextCar();
    }

    //region runTurn privates
    private int move() {
        Rail tmp = rail;
        try {
            rail = rail.carMoves(this, prevRail);
            if (rail == stationWhereEmpty) {
                empty = true;
            }
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
    //endregion


    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    /**
     *  If c equals the Cars Color and canEmpty is true, sets the Cars Color to {@link Color#NO_COLOR} and calls the
     *  {@link Car}s behind it, not to empty.
     * @param s The Color to compare.
     */
    public void atStation(Station s) {
        if (empty && s.removePassenger(color)) {
            empty = false;
        } else if(canEmpty){
            if(!empty){
                if(next != null) {
                    next.setCanEmpty(false);
                }
            }
            if(color == s.getColor()){
                empty = true;
            }
        }
    }

    public void setCanEmpty(boolean b) { MethodPrinter.enterMethod();
        canEmpty = b;
        if(next != null) {
            next.setCanEmpty(b);
        }
        MethodPrinter.leaveMethod();
    }

    public boolean isEmpty() {
        return empty;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Car{" +
                "rail=" + rail.id +
                ", prevRail=" + prevRail.id +
                ", color=" + color +
                ", empty=" + empty +
                ",\n\tnext=" + next +
                '}';
    }
}