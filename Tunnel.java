
/**
 * Represents a tunnel object. A tunnel is a special {@link Rail}, because it is not always presented.
 */
public class Tunnel extends Rail {

//    protected Rail fromsNeighbor;
//    protected Rail tosNeighbor;


    /**
     * Creates a new {@link Tunnel} object.
     * @param from End of the Tunnel.
     * @param to End of the Tunnel.
     */
    public Tunnel(Rail from, Rail to) {
        super(from, to);
    }

    /**
     * Builds the {@link Tunnel}s end to r {@link Rail#to} end. If 'from' is not yet linked to a rail, then that one, if it is then 'to'.
     * @param r The {@link Rail}.
     */
    public void buildTunnel(Rail r) {
	    if(this.car==null){
	    	if(from == null && to != r) {
//	            fromsNeighbor = r.getTo();
	            this.from = r;
	        } else if(to == null && from != r) {
//	            tosNeighbor = r.getTo();
	            to = r;
	        }
            if (r.getFrom() == null) {
                r.setFrom(this);
            } else {
                r.setTo(this);
            }
        }
    }

    /**
     * Destroy both ends of the {@link Tunnel}.
     */
    public boolean destroyTunnel() {
        if(car == null && from.car == null && to.car == null){
	        //from.setTo(fromsNeighbor);
	        //to.setTo(tosNeighbor);
            restoreEnd(from);
            restoreEnd(to);
            System.out.println(from);
            System.out.println(to);
	        from = null;
	        to = null;
	        return true;
        }
        return false;
    }

    private void restoreEnd(Rail r) {
        if (r.getFrom() == this) {
            r.setFrom(null);
        } else {
            r.setTo(null);
        }
    }

    @Override
    public String toString() { 
        return "Tunnel{" +
        		super.toString()+", "+ "}";
//                "fromsNeighbor=" + fromsNeighbor +
//                ", tosNeighbor=" + tosNeighbor + "}";
    }
}