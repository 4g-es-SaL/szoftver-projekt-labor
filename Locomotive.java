
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
        int ret = 1;
        for (int i = 0; i < speed; i++) {
            Rail tmp = rail;
            rail = rail.carMoves(this, prevRail);
            prevRail = tmp;
            if(next != null){
                ret = next.runTurn();
                if(ret == 2){
                    System.out.println("ures");
                }
            }

        }
        System.out.println(rail.id);
        return ret;
    }
    /**
     * 
     */
    protected int speed;

    /**
     * @param rail 
     * @param prev 
     * @param next 
     * @param speed
     */

}