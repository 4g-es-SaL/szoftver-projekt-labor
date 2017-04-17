
/**
 * Represents a {@link Locomotive}. A {@link Locomotive} is a special type of {@link Car}. It can have a speed.
 */
public class Locomotive extends Car {
    protected int speed;
    protected int entryTime;

    /**
     * Create a new {@link Locomotive} object. Locomotives {@link Color} is {@link Color#NO_COLOR}.
     * @param rail The Locomotive will stand on this {@link Rail}.
     * @param prevRail The Locomotive has come from this {@link Rail}.
     * @param next In the train, the {@link Car} behind this {@link Car}.
     * @param speed The speed of the {@link Locomotive}.
     */
    public Locomotive(Rail rail, Rail prevRail, Car next, int speed, int entryTime) throws Exception {
        super(rail, prevRail, next,Color.NO_COLOR);
        if (next != null && next.getClass().equals(Locomotive.class)) {
            //TODO: Free Rail from Car
            System.exit(1);
            throw new Exception("A train cant have multiple Locomotives.");
        }
        this.speed = speed;
        this.entryTime = entryTime;
        this.empty = true;
    }

    /**
     * The Locomotive moves to the next {@link Rail} in the network. Repeats it 'speed' times. Pulls the {@link Car} behind it.
     * @return 1 if there was a collision, 0 otherwise.
     */
    public int runTurn() { MethodPrinter.enterMethod();
        int res = 0;
        for (int i = 0; i < speed; i++) {
            res = super.runTurn();
            if (res == 1) {
                MethodPrinter.leaveMethod();
                return 1;
            }
        }
        System.out.println(this);
        MethodPrinter.leaveMethod(); return res;
    }


    /**
     * Notifies the {@link Car}s behind it, that they can empty.
     * @param s The {@link Station} on which the Locomotive arrives.
     */
    @Override
    public void atStation(Station s) { MethodPrinter.enterMethod();
        if (next != null) {
            next.setCanEmpty(true);
        }
        MethodPrinter.leaveMethod();
    }

    @Override
    public String toString() {
        return "Locomotive{" + super.toString() +
                ", speed=" + speed +
                "}";
    }

    // helper function for reading in
    public Car getLastCar(){
        Car tmp = this;
        while (tmp.next != null)
            tmp = tmp.next;
        return tmp;
    }
}