
/**
 * Created by matech on 2017. 02. 20..
 */
public class Tunnel extends Rail {

    protected Rail fromsNeighbor;
    protected Rail tosNeighbor;


    /**
     * @param from
     * @param to
     */
    public Tunnel(Rail from, Rail to) {
        super(from, to);
        MethodPrinter.enterMethod();
        MethodPrinter.leaveMethod();
    }

    /**
     * @param r
     */
    public void buildTunnel(Rail r) {
        MethodPrinter.enterMethod();
        if(from == null) {
            fromsNeighbor = r.getTo();
            r.setTo(this);
            from = r;
        } else if(to == null) {
            tosNeighbor = r.getFrom();
            r.setTo(this);
            to = r;
        }

        MethodPrinter.leaveMethod();
    }

    /**
     *
     */
    public void destroyTunnel() {
        MethodPrinter.enterMethod();

        from.setTo(fromsNeighbor);
        to.setTo(tosNeighbor);
        from = null;
        to = null;

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