
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
        super(from, to);
        alternativeWays = to2;
    }

    protected ArrayList<Rail> alternativeWays;
    protected int currentToId = 0;


    public void changeDir() {
        currentToId = (currentToId+1) % (alternativeWays.size()+1);
    }

    public Rail carMoves(Car t, Rail prev) {
        if(from == prev) {
            if (currentToId == 0)
                return to;
            return alternativeWays.get(currentToId - 1);
        }
        else
            return from;
    }

    @Override
    public String toString() {
        return "Switch{" +
               super.toString() + "}";
    }
}