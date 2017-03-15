
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.util.*;
import java.io.Reader;

/**
 * Created by matech on 2017. 02. 20..
 */
public class Program {

    /**
     *
     */
    protected Playground playground;


    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO implement here
        File f = new File("map.txt");
        Playground playground1 = new Playground(f);

        playground1.buildTunnelEnd(0);
        playground1.buildTunnelEnd(1);
        for (int i = 0; i < 20; i++) {
            playground1.runTurn();
            if(i == 11)
                playground1.changeSwitch(1);
        }

    }

}