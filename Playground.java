import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;

/**
 * Created by matech on 2017. 02. 20..
 */
public class Playground {

    /**
     *
     * @param f
     *
     * A file tartalma a következő
     * R
     * from0 to0 [sz]
     * from1 to1 [sz]
     * ...
     * fromR-1 toR-1 [sz]
     * S
     * from0 to0 alt00 alt01 ...
     * from1 to1 alt10 alt11 ...
     * ...
     * fromS-1 toS-1 altS-10 altS-11 ...
     * t0 t1 t2 ...
     *
     * ahol R = #Rails + #Stations
     * a következő R sorban:
     * fromi és toi mutaja, hogy az i-dik sínnek ki a
     * fromja és toja, és ha az i-dik éppen station,
     * akkor 3.ként ott egy szín is
     *
     * S = #Switches
     * következő S sorban:
     * fromi, toi, alti0, alti1, ... mutatják az i-dik
     * Switch dolgait
     *
     * Következő sor:
     * A lehetséges Tunnel építési pontok
     *
     * Utolsó sor:
     * A belépő pontok listája
     */

    Playground(File f) {

        rails = new ArrayList<>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(f));
            String[] line;


            // Reading Rails and Stations
            int numRails = Integer.parseInt(in.readLine());
            System.out.println(numRails);
            for (int i = 0; i < numRails; i++) {
                line = in.readLine().split(" ");
                int from = Integer.parseInt(line[0]);
                int to = Integer.parseInt(line[1]);

                Rail r;

                if(line.length > 2){
                    Color c = Color.values()[Integer.parseInt(line[2])];
                    r = new Station(null, null, c);
                }
                else
                    r = new Rail(null, null);

                if (from != -1 && from < rails.size()) {
                    r.setFrom(rails.get(from));
                    if (rails.get(from).to == null)
                        rails.get(from).setTo(r);
                }
                if (to != -1 && to < rails.size()){
                    r.setTo(rails.get(to));
                    if(rails.get(to).from == null)
                        rails.get(to).setFrom(r);
                }
                rails.add(r);
            }

            // Reading Switches
            int numSwitches = Integer.parseInt(in.readLine());
            System.out.println(numSwitches);
            for (int i = 0; i < numSwitches; i++) {
                line = in.readLine().split(" ");
                int from = Integer.parseInt(line[0]);
                int to = Integer.parseInt(line[1]);
                ArrayList<Rail> alt = new ArrayList<>();

                for (int j = 2; j < line.length; j++) {
                    alt.add(rails.get(Integer.parseInt(line[j])));
                }
                Switch sw = new Switch(rails.get(from), rails.get(to), alt);
                rails.add(sw);

                rails.get(from).setTo(sw);
                rails.get(to).setFrom(sw);
                for (int j = 2; j < line.length; j++) {
                    if(rails.get(Integer.parseInt(line[j])).from == null)
                        rails.get(Integer.parseInt(line[j])).setFrom(sw);
                }
            }

            rails.add(new Tunnel(null, null));

            // Setting up tunnel end points
            line = in.readLine().split(" ");
            for (int i = 0; i < line.length; i++) {
                int idx = Integer.parseInt(line[i]);
                tunnelEndPoints.add(rails.get(idx));
            }

            // Setting up entry points
            line = in.readLine().split(" ");
            for (int i = 0; i < line.length; i++) {
                int idx = Integer.parseInt(line[i]);
                enterPoints.add(rails.get(idx));
            }

            for (int i = 0; i < numRails; i++) {
                System.out.println(i + ": " + rails.indexOf(rails.get(i).from) + " - " + rails.indexOf(rails.get(i).to));
            }

            locomotives.add(new Locomotive(rails.get(0), rails.get(7), null, 1));

        } catch (IOException e){
            System.out.println(e.toString());
        }
    }


    protected long startTime;
    protected ArrayList<Locomotive> locomotives = new ArrayList<>();
    protected ArrayList<Rail> rails;
    protected ArrayList<Rail> enterPoints = new ArrayList<>();
    private ArrayList<Rail> tunnelEndPoints = new ArrayList<>();


    /**
     * @return
     */
    public int runTurn() {
        // TODO implement here
        for (Locomotive loc:locomotives) {
            System.out.println(rails.indexOf(loc.runTurn()));
        }
        return 1;
    }

    /**
     * @param id
     * @return
     */
    public void changeSwitch(int id) {
        // TODO implement here
        throw new NotImplementedException();
    }

    /**
     * @param id
     * @return
     */
    public void buildTunnelEnd(int id) {
        // TODO implement here
        throw new NotImplementedException();
    }

    /**
     * @param id
     * @return
     */
    public void destroyTunnelEnd(int id) {
        // TODO implement here
        throw new NotImplementedException();
    }

}