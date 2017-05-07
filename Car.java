/**
 * Represents a {@link Car}. A {@link Car} can move on {@link Rail}s.
 */
public class Car {

    protected final Color color;
    protected Rail rail;
    protected Rail prevRail;
    protected Car next;
    protected boolean empty;
    protected Station stationWhereEmpty;
    protected int spawn;
    private int canEmpty;

    /**
     * Creates a new {@link Car} object and notifies the {@link Rail}s about its presence.
     *
     * @param rail     The {@link Car} will stand on this Rail.
     * @param prevRail The {@link Car} has come from this Rail.
     * @param next     In the train, the {@link Car} behind this {@link Car}.
     * @param color    The {@link Color} of the {@link Car}.
     */
    public Car(Rail rail, Rail prevRail, Car next, Color color, int spawn) throws Exception {
        this.rail = rail;
        this.prevRail = prevRail;
        this.next = next;
        this.spawn = spawn;
        this.color = color;
        if (color == Color.NO_COLOR) {
            empty = true;
        }
        canEmpty = 0;
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
     *
     * @return 1 if there was a collision, 0 otherwise.
     */
    public int runTurn() {
        Integer x = waitOrSpawn();
        if (x != null) return x;

        int isCollision = move();
        if (isCollision == 1) {
            return isCollision;
        }

        return callNextCar();
    }

    protected Integer waitOrSpawn() {
        if (spawn > 0) {
            if (spawn == 1) {
                try {
                    signUpCarOnRail(rail, prevRail);
                } catch (Exception e) {
                    e.printStackTrace();
                    return 1;
                }
            }
            spawn--;
            return callNextCar();
        }
        return null;
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
            e.printStackTrace();
            return 1;
        }
        prevRail = tmp;
        return 0;
    }

    private int callNextCar() {
        if (next != null) {
            return next.runTurn();
        }
        return 0;
    }
    //endregion

    /**
     * Handles itself according to the given situation, empties itself, fills itself or forbids emptying behind itself.
     *
     * @param s The station where the car arrives.
     */
    public void atStation(Station s) {
        if (empty && s.removePassenger(color)) {
            empty = false;
            setCanEmpty(false);
        } else if (canEmpty > 0) {
            if (color == s.getColor()) {
                empty = true;
                canEmpty = 0;
            }
            if (!empty) {
                setCanEmpty(false);
            }
        }
    }

    /**
     * Sets whether the Car is allowed to be emptied.
     *
     * @param b True if you want to allow emptying, false otherwise.
     */
    public void setCanEmpty(boolean b) {
        if (b) {
            canEmpty++;
        } else {
            canEmpty--;
        }
        if (next != null) {
            next.setCanEmpty(b);
        }
    }

    /**
     * Tests whether the whole train is empty.
     *
     * @return True if it is empty, false otherwise.
     */
    public boolean isTrainEmpty() {
        return empty && (next == null || next.isTrainEmpty());
    }

    /**
     * Tests whether the car is empty.
     *
     * @return True if empty, false otherwise.
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * Sets whether the car is empty or not.
     *
     * @param empty True, if you want to set it to empty, false otherwise.
     */
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    /**
     * Gives back the colour of the Car.
     *
     * @return The colour ({@link Color}).
     */
    public Color getColor() {
        return color;
    }

    public Rail getCurrentRail() {
        if (spawn == 0) {
            return rail;
        } else {
            return null;
        }
    }

    public boolean hasSpawn() {
        return spawn == 0;
    }

    @Override
    public String toString() {
        return "Car{" +
                "rail=" + rail.id +
                ", prevRail=" + prevRail.id +
                ", color=" + color +
                ", empty=" + empty +
                //", canEmpty=" + canEmpty +
                ",\n\tnext=" + next +
                '}';
    }
}