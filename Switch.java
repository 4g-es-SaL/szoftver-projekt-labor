
import java.util.ArrayList;

/**
 * Created by matech on 2017. 02. 20..
 */
public class Switch extends Rail {

    /**
     * @param from
     * @param to
     * @param to2
     */
    public Switch(Rail from, Rail to, ArrayList<Rail> to2) {
        super(from, to); MethodPrinter.enterMethod();
        alternativeWays = to2;

        MethodPrinter.leaveMethod();
    }

    protected ArrayList<Rail> alternativeWays;
    protected int currentToId = 0;


    public void changeDir() { MethodPrinter.enterMethod();
        currentToId = (currentToId+1) % (alternativeWays.size());
        to = alternativeWays.get(currentToId);

        MethodPrinter.leaveMethod();
    }

    @Override
    public String toString() {
        return "Switch{" +
               super.toString() + "}";
    }
}