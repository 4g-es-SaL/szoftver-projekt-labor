import java.util.Iterator;

/**
 * Represents a {@link Locomotive}. A {@link Locomotive} is a special type of {@link Car}. It can have a speed.
 */
public class Locomotive extends Car implements Iterable<Car> {
    protected int speed;

    /**
     * Create a new {@link Locomotive} object. Locomotives {@link Color} is {@link Color#NO_COLOR}.
     * @param rail The Locomotive will stand on this {@link Rail}.
     * @param prevRail The Locomotive has come from this {@link Rail}.
     * @param next In the train, the {@link Car} behind this {@link Car}.
     * @param speed The speed of the {@link Locomotive}.
     */
    public Locomotive(Rail rail, Rail prevRail, Car next, int speed, int entryTime) throws Exception {
        super(rail, prevRail, next,Color.NO_COLOR, entryTime);
        if (next != null && next.getClass().equals(Locomotive.class)) {
            //TODO: Free Rail from Car
            System.exit(1);
            throw new Exception("A train cant have multiple Locomotives.");
        }
        this.speed = speed;
        this.empty = true;
    }

    /**
     * The Locomotive moves to the next {@link Rail} in the network. Repeats it 'speed' times. Pulls the {@link Car} behind it.
     * @return 1 if there was a collision, 0 otherwise.
     */
    public int runTurn() {
        Integer x = waitOrSpawn();
        if (x != null) return x;
        for (int i = 0; i < speed; i++) {
            int res = super.runTurn();
            if (res == 1) {
                return 1;
            }
        }
        System.out.println(this);
        return 0;
    }

    /**
     * Notifies the {@link Car}s behind it, that they can empty.
     * @param s The {@link Station} on which the Locomotive arrives.
     */
    @Override
    public void atStation(Station s) {
        if (next != null) {
            next.setCanEmpty(true);
        }
    }

    @Override
    public String toString() {
        return "Locomotive{" + super.toString() +
                ", speed=" + speed +
                "}";
    }

    // region helper function for reading in
    public Car getLastCar(){
        Car tmp = this;
        while (tmp.next != null)
            tmp = tmp.next;
        return tmp;
    }
    //endregion

    @Override
    public Iterator<Car> iterator() {
        return new Iterator<Car>() {
            private Car nextCar = Locomotive.this;
            @Override
            public boolean hasNext() {
                return nextCar != null;
            }

            @Override
            public Car next() {
                Car res = nextCar;
                nextCar = nextCar.next;
                return res;
            }
        };
    }
}