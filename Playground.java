import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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

    Playground(File f) {          MethodPrinter.enterMethod();

        rails = new ArrayList<>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(f));
            String[] line;

            tunnel = new Tunnel(null, null);
            Rail.idGenerator--;
            // Reading Rails and Stations
            int numRails = Integer.parseInt(in.readLine());
//            System.out.println(numRails);
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
                else if(from == -1)
                    r.setFrom(tunnel);

                if (to != -1 && to < rails.size()){
                    r.setTo(rails.get(to));
                    if(rails.get(to).from == null)
                        rails.get(to).setFrom(r);
                }
                else if(to == -1)
                    r.setTo(tunnel);
                rails.add(r);
            }

            // Reading Switches
            int numSwitches = Integer.parseInt(in.readLine());
//            System.out.println(numSwitches);
            for (int i = 0; i < numSwitches; i++) {
                line = in.readLine().split(" ");
                int from = Integer.parseInt(line[0]);
                int to = Integer.parseInt(line[1]);
                ArrayList<Rail> alt = new ArrayList<>();
                Rail fromR, toR;
                if(from == -1)
                    fromR = tunnel;
                else
                    fromR = rails.get(from);

                if(to == -1)
                    toR = tunnel;
                else
                    toR = rails.get(to);

                Switch sw = new Switch(fromR, toR, alt);
                rails.add(sw);
                switches.add(sw);

                for (int j = 2; j < line.length; j++) {
                    int idx = Integer.parseInt(line[j]);

                    if(idx == -1)
                        sw.alternativeWays.add(tunnel);
                    else {
                        sw.alternativeWays.add(rails.get(idx));
                        if (rails.get(idx).to == null)
                            rails.get(idx).setTo(sw);
                        else
                            rails.get(idx).setFrom(sw);
                    }
                }

                if(from != -1)
                    if(rails.get(from).to == null)
                        rails.get(from).setTo(sw);
                    else
                        rails.get(from).setFrom(sw);

                if(to != -1)
                    if(rails.get(to).from == null)
                        rails.get(to).setFrom(sw);
                    else
                        rails.get(to).setTo(sw);

                for (int j = 2; j < line.length; j++) {
                    int idx = Integer.parseInt(line[j]);
                    if(idx != -1)
                        if(rails.get(idx).from == null)
                            rails.get(idx).setFrom(sw);
                }
                if(rails.get(from).to == null)
                    rails.get(from).setTo(sw);
            }

            tunnel.id = rails.size();
            rails.add(tunnel);

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

//            for (int i = 0; i < numRails; i++) {
//                System.out.println(i + ": " + rails.indexOf(rails.get(i).from) + " - " + rails.indexOf(rails.get(i).to));
//            }

            Car car2 = new Car(rails.get(7), rails.get(4), null, Color.BLUE);
            car2.canEmpty = false;
            Car car1 = new Car(rails.get(5), rails.get(7), car2, Color.BLUE);
            car1.canEmpty = true;
            locomotives.add(new Locomotive(rails.get(0), rails.get(5), car1, 1));

        } catch (IOException e){
            System.out.println(e.toString());
        }
        MethodPrinter.leaveMethod();
    }


    protected long startTime;
    protected ArrayList<Locomotive> locomotives = new ArrayList<>();
    protected ArrayList<Rail> rails;
    protected ArrayList<Rail> enterPoints = new ArrayList<>();
    protected Tunnel tunnel;
    private ArrayList<Rail> tunnelEndPoints = new ArrayList<>();
    private ArrayList<Switch> switches = new ArrayList<>();


    /**
     * @return
     */
    public int runTurn() {          MethodPrinter.enterMethod();
        // TODO implement here

        int res = 0;
        for (Locomotive loc:locomotives) {
            res = loc.runTurn();
            if (res == 1) {
                MethodPrinter.leaveMethod(); return res;
            }
        }
        MethodPrinter.leaveMethod(); return res;

    }

    /**
     * @param id
     * @return
     */
    public void changeSwitch(int id) {          MethodPrinter.enterMethod();
        MethodPrinter.enterMethod();
        switches.get(id).changeDir();
        MethodPrinter.leaveMethod();
    }

    /**
     * @param id
     * @return
     */
    public void buildTunnelEnd(int id) {         MethodPrinter.enterMethod();
        tunnel.buildTunnel(tunnelEndPoints.get(id));
    }

    /**
     * @param id
     * @return
     */
    public void destroyTunnelEnd(int id) {          MethodPrinter.enterMethod();
        // TODO implement here
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        String res = "Playground{\n";
        for (Rail r :
                rails) {
            res += r + "\n";
        }
        res += "}";
        return res;
    }
}