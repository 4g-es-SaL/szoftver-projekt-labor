
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Created by matech on 2017. 02. 20..
 */
public class Switch extends Rail {

    /**
     * @param from
     * @param to
     * @param to2
     */
    public Switch(Rail from, Rail to, Set<Rail> to2) {
        super(from, to);
        // TODO implement here
        throw new NotImplementedException();
    }

    /**
     * 
     */
    protected Set<Rail> alternativeWays;

    /**
     * 
     */
    protected int currentToId = 0;



    /**
     *
     */
    public void changeDir() {
        // TODO implement here
        throw new NotImplementedException();
    }

}