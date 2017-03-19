
/**
 * Created by matech on 2017. 02. 20..
 */
public class Locomotive extends Car {
    protected int speed;

    /**
     * Create a new Locomotive object. Locomotives Color is NO_COLOR.
     * @param rail The Locomotive will stand on this Rail.
     * @param prevRail The Locomotive has come from this Rail.
     * @param next In the train, the Car behind this Car.
     * @param speed The speed of the Locomotive.
     */
    public Locomotive(Rail rail, Rail prevRail, Car next, int speed) throws Exception {
        super(rail, prevRail, next,Color.NO_COLOR);
        this.speed = speed;
    }

    /**
     * The Locomotive moves to the next Rail in the network. Repeats it with speed time. Pulls the Car behind it.
     * @return 1 if there was a collision, 0 otherwise.
     */
    public int runTurn() { MethodPrinter.enterMethod();
//        System.out.print(rail.id + " ");
        int res = 0;
        for (int i = 0; i < speed; i++) {
            res = super.runTurn();
            if (res == 1) {
                MethodPrinter.leaveMethod();
                return 1;
            }
        }
//        System.out.print("\n\n");
        MethodPrinter.leaveMethod(); return res;
    }

    @Override
    public void atStation(Color c) { MethodPrinter.enterMethod();
        if (next != null) {
            next.setCanEmpty(true);
        }
        MethodPrinter.leaveMethod();
    }
}