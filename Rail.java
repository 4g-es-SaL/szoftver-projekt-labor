
/**
 * Represents a {@link Rail} object.
 */
public class Rail {

    /**
     * Create a new {@link Rail} object.
     * @param from Connected to this {@link Rail}.
     * @param to Connected to this {@link Rail}. If Tunnel will build this neighbor will be replaced.
     */
    public Rail(Rail from, Rail to) {
        MethodPrinter.enterMethod();
        id = idGenerator++;
        this.from = from;
        this.to = to;
        MethodPrinter.leaveMethod();
    }

    public static int idGenerator = 0;
    protected int id;
    protected Rail from;
    protected Rail to;
    protected Car car;

    /**
     * Moves the {@link Car} to the next Rail in the network.
     * @param c The {@link Car} that moves.
     * @param prev The Rail where the {@link Car} came from.
     * @return The {@link Rail} where c stands.
     * @throws Exception In occasion of collision!
     */
    public Rail carMoves(Car c, Rail prev) throws Exception {
//        for (int i = 0; i < MethodPrinter.tabCount+1; i++)  System.out.print("\t");
//        System.out.println(this.toString());
        MethodPrinter.enterMethod();

        car = null;
        Rail nextRail = getNextRail(prev);
        try {
            nextRail.addCar(c);
        } catch (Exception e) {
            MethodPrinter.leaveMethod();
            throw e;
        }

        MethodPrinter.leaveMethod();
        return nextRail;
    }

    protected Rail getNextRail(Rail prev) {
        if(from == prev) {
            return to;
        } else {
            return from;
        }
    }

    protected void addCar(Car c) throws Exception {
        if (car != null) {
            throw new Exception("Collision!");
        } else {
            car = c;
        }
    }
    
    /**
     * Sets 'from' of the rail.
     * @param r Rail to be set.
     */
    public void setFrom(Rail r) { MethodPrinter.enterMethod();
        from = r;

        MethodPrinter.leaveMethod();
    }

    /**
     * Sets 'to' of the rail.
     * @param r Rail to be set.
     */
    public void setTo(Rail r) { MethodPrinter.enterMethod();
        to = r;

        MethodPrinter.leaveMethod();
    }

    /**
     * Gives 'from' back.
     * @return Rail 'from' of this rail.
     */
    public Rail getFrom() { //MethodPrinter.enterMethod();
//    MethodPrinter.leaveMethod();
        return from;
    }

    /**
     * Gives 'to' back.
     * @return Rail 'to' of this rail.
     */
    public Rail getTo() { //MethodPrinter.enterMethod();
//        MethodPrinter.leaveMethod();
        return to;
    }

    /**
     * Gives back the id of the rail.
     * @return The id.
     */
    public int getId(){return id;}

    @Override
    public String toString() {
        return "Rail{" +
                "id=" + id +
                ", from=" + (from != null ? from.id : "-1") +
                ", to=" + (to != null ? to.id : "-1") +
                '}';
    }
}