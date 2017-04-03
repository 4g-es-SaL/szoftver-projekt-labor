
/**
 * Represents a tunnel object. A tunnel is a special {@link Rail}, because it is not always presented.
 */
public class Tunnel extends Rail {

    protected Rail fromsNeighbor;
    protected Rail tosNeighbor;


    /**
     * Create a new {@link Tunnel} object.
     * @param from
     * @param to
     */
    public Tunnel(Rail from, Rail to) {
        super(from, to);
        MethodPrinter.enterMethod();
        MethodPrinter.leaveMethod();
    }

    /**
     * Builds the {@link Tunnel}s end to r {@link Rail#to} end.
     * @param r
     */
    public void buildTunnel(Rail r) {
        MethodPrinter.enterMethod();
	    if(this.car==null){
	        if(from == null) {
	            fromsNeighbor = r.getTo();
	            r.setTo(this);
	            from = r;
	        } else if(to == null) {
	            tosNeighbor = r.getFrom();
	            r.setTo(this);
	            to = r;
	        }
        }
        MethodPrinter.leaveMethod();
    }

    /**
     * Destroy both end of the {@link Tunnel}.
     */
    public void destroyTunnel() {
        MethodPrinter.enterMethod();
        if(this.car==null){
	        from.setTo(fromsNeighbor);
	        to.setTo(tosNeighbor);
	        from = null;
	        to = null;
        }
        MethodPrinter.leaveMethod();
    }

    @Override
    public String toString() {
        return "Tunnel{" +
                "fromsNeighbor=" + fromsNeighbor +
                ", tosNeighbor=" + tosNeighbor +
                super.toString() + "} ";
    }
}