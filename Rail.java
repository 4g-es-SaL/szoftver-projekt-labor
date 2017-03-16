
/**
 * Created by matech on 2017. 02. 20..
 */
public class Rail {

    /**
     * Create a new Rail object.
     * @param from Connected to this Rail.
     * @param to Connected to this Rail. If Tunnel will build this neighbor will be replaced.
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
     * Moves the Car to the next Rail in the network.
     * @param c The Car that moves.
     * @param prev The Rail where the Car came from.
     * @return The Rail where c stands.
     * @throws Exception In occasion of collision!
     */
    public Rail carMoves(Car c, Rail prev) throws Exception {
        for (int i = 0; i < MethodPrinter.tabCount+1; i++)  System.out.print("\t");
        System.out.println(this.toString());
        MethodPrinter.enterMethod();

        c = null;
        Rail nextRail = getNextRail(prev);
        nextRail.addCar(c);

        MethodPrinter.leaveMethod();
        return nextRail;
    }

    private Rail getNextRail(Rail prev) {
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
     * @param r
     * @return
     */
    public void setFrom(Rail r) { MethodPrinter.enterMethod();
        from = r;

        MethodPrinter.leaveMethod();
    }

    /**
     * @param r
     * @return
     */
    public void setTo(Rail r) { MethodPrinter.enterMethod();
        to = r;

        MethodPrinter.leaveMethod();
    }

    public Rail getFrom() { MethodPrinter.enterMethod();
    MethodPrinter.leaveMethod();
        return from;
    }

    public Rail getTo() { MethodPrinter.enterMethod();
        MethodPrinter.leaveMethod();
        return to;
    }

    @Override
    public String toString() {
        return "Rail{" +
                "id=" + id +
                ", from=" + (from != null ? from.id : "-1") +
                ", to=" + (to != null ? to.id : "-1") +
                '}';
    }
}