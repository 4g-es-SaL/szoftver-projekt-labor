
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

        for (int i = 0; i < 20; i++) {
            playground1.runTurn();
        }

    }

}