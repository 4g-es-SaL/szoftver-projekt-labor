
/**
 * Created by matech on 2017. 02. 20..
 */
public class Locomotive extends Car {

    /**
     *
     * @param rail
     * @param prevRail
     * @param next
     * @param speed
     */
    public Locomotive(Rail rail, Rail prevRail, Car next, int speed) {
        super(rail, prevRail, next,Color.NO_COLOR);
        this.speed = speed;
    }

    public int runTurn() {
        System.out.print(rail.id + " ");
        int res = 0;
        for (int i = 0; i < speed; i++) {
            res = super.runTurn();
            if (res == 1) {
                return 1;
            }
        }
        System.out.print("\n\n");
        return res;
    }
    /**
     * 
     */
    protected int speed;

}