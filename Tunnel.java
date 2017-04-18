
/**
 * Represents a tunnel object. A tunnel is a special {@link Rail}, because it is not always presented.
 */
public class Tunnel extends Rail {

    protected Rail fromsNeighbor;
    protected Rail tosNeighbor;


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
	    	if(from == null) {
	            fromsNeighbor = r.getTo();
	            r.setTo(this);
	            from = r;
	        } else if(to == null) {
	            tosNeighbor = r.getTo();
	            r.setTo(this);
	            to = r;
	        }
        }
    }

    /**
     * Destroy both ends of the {@link Tunnel}.
     */
    public void destroyTunnel() {
        if(this.car==null){
	        from.setTo(fromsNeighbor);
	        to.setTo(tosNeighbor);
	        from = null;
	        to = null;
        }
    }

    @Override
    public String toString() { 
        return "Tunnel{" +
        		super.toString()+", "+
                "fromsNeighbor=" + fromsNeighbor +
                ", tosNeighbor=" + tosNeighbor + "}";
    }
}