
import java.util.ArrayList;

/**
 * Represents a switch object. A switch is a special type of {@link Rail}. It can have not just 2, but multiple ends,
 * but only 2 can be active.
 */
public class Switch extends Rail {

    protected ArrayList<Rail> alternativeWays;
    protected int currentToId = 0;

    /**
     * Create a {@link Switch} object.
     * @param from Connected to this {@link Rail}. If {@link Car} comes from one of the to2 direction, it will go this way.
     * @param to Connected to this {@link Rail}.
     * @param to2 to can be replaced with this ones. Must contain to too.
     */
    public Switch(Rail from, Rail to, ArrayList<Rail> to2) {
        super(from, to);
        alternativeWays = to2;
    }


    /**
     * Rotates alternativeWays. After the last one it flips to the first one again.
     */
    public void changeDir() {
        if(car == null){
            currentToId = (currentToId+1) % (alternativeWays.size());
            to = alternativeWays.get(currentToId);
            System.out.println("alternativeWays of Switch " + id + " changed to " + to.getId());
        }else{
            System.out.println("Switch cannot be changed!");
        }
    }

    public ArrayList<Rail> getTos() {
        return alternativeWays;
    }

    @Override
    public String toString() {
        return "Switch{" +
               super.toString() +
                ", alternativeWays=" + getTosId() + "}";
    }

    /**
     * If you can't figure out by the name, you are not worthy to be called 'programmer'.
     */
    private ArrayList<Integer> getTosId() {
        ArrayList<Integer> res = new ArrayList<>();
        for (Rail r :
                alternativeWays) {
            res.add(r.id);
        }
        return res;
    }
}