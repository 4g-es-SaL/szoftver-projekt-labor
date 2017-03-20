
/**
 * Represents a {@link Car}. A {@link Car} can move on {@link Rail}s.
 */
public class Car {

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

    private void signUpCarOnRail(Rail rail, Rail prevRail) throws Exception {
        Rail prevFrom = prevRail.getFrom();
        Rail prevTo = prevRail.getTo();
        if (rail == prevFrom) {
            prevRail.carMoves(this, prevTo);
        } else {
            prevRail.carMoves(this, prevFrom);
        }
    }

    protected Rail rail;
    protected Rail prevRail;
    protected Car next;
    protected Color color;
    protected boolean canEmpty;


    /**
     * The {@link Car} moves to the next {@link Rail} in the network. Pulls the {@link Car} behind it.
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
     *  If c equals the Cars Color and canEmpty is true, sets the Cars Color to {@link Color#NO_COLOR} and calls the
     *  {@link Car}s behind it, not to empty.
     * @param c The Color to compare.
     */
    public void atStation(Color c) { MethodPrinter.enterMethod();
    	if(canEmpty){
    		if(color != Color.NO_COLOR){
    			if(next != null) {
    				next.setCanEmpty(false);
    			}
    		}
    		if(color == c){
    			color = Color.NO_COLOR;
    		}
    	}
        MethodPrinter.leaveMethod();
    }

    /**
     * Sets the canEmpty value.
     * @param b
     */
    public void setCanEmpty(boolean b) { MethodPrinter.enterMethod();
        canEmpty = b;
        if(next != null) {
            next.setCanEmpty(b);
        }
        MethodPrinter.leaveMethod();
    }

}