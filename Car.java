
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
    public int runTurn() { MethodPrinter.enterMethod();
        int isCollision = move();
        if (isCollision == 1) {
            MethodPrinter.leaveMethod(); return isCollision;
        }

        int res = callNextCar();
        MethodPrinter.leaveMethod(); return res;
    }

    //region runTurn privates
    private int move() {
        Rail tmp = rail;
        try {
            if (rail == stationWhereEmpty) {
                empty = true;
            }
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
    //endregion

    /**
     *  If c equals the Cars Color and canEmpty is true, sets the Cars Color to {@link Color#NO_COLOR} and calls the
     *  {@link Car}s behind it, not to empty.
     * @param s The station compare.
     */
    public void newStation(Station s) {
    //TODO: Reimplement
        if(empty || stationWhereEmpty != null) {
            if (next != null) {
                next.newStation(s);
            }
        } else {
            if (color == s.getColor()) {
                stationWhereEmpty = s;
            }
        }
    }

    public boolean isEmpty() {
        return empty;
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