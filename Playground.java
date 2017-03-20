import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Maneges the objects({@link Rail}s, {@link Car}s) of the game.
 */
public class Playground {

    protected long startTime;
    protected ArrayList<Locomotive> locomotives = new ArrayList<>();
    protected ArrayList<Rail> rails = new ArrayList<>();
    protected ArrayList<Rail> enterPoints = new ArrayList<>();
    protected Tunnel tunnel;
    private ArrayList<Rail> tunnelEndPoints = new ArrayList<>();
    private ArrayList<Switch> switches = new ArrayList<>();

    /**
     * Creat a new instance of Playground
     * @param f The file that stores the map of {@link Rail}s.
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
        MethodPrinter.enterMethod();

        try(BufferedReader in = new BufferedReader(new FileReader(f))) {
            readRails(in);
            readSwitches(in);
            readTunnelEndPoints(in);
            readEntryPoints(in);

            // create and add the only Tunnel object to the rails
            tunnel = new Tunnel(null, null);
            rails.add(tunnel);

        } catch (Exception e){
            System.out.println(e.toString());
        }

        MethodPrinter.leaveMethod();
    }

    /**
     * Calls all locomotives {@link Locomotive#runTurn()}.
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
     * Changes the {@link Switch} direction.
     * @param id Identifies the Switch.
     */
    public void changeSwitch(int id) {          MethodPrinter.enterMethod();
        switches.get(id).changeDir();
        MethodPrinter.leaveMethod();
    }

    /**
     * @param id
     * @return
     */
    public void buildTunnelEnd(int id) {         MethodPrinter.enterMethod();
        tunnel.buildTunnel(tunnelEndPoints.get(id));
        MethodPrinter.leaveMethod();
    }

    /**
     * @param id
     * @return
     */
    public void destroyTunnelEnd(int id) {
        MethodPrinter.enterMethod();
        tunnel.destroyTunnel();
        MethodPrinter.leaveMethod();
    }

    public void initializeA() {
        Locomotive l = null;
        try {
            l = new Locomotive(rails.get(6), rails.get(1),
                    new Car(rails.get(1), rails.get(2),
                            new Car(rails.get(2), rails.get(3),
                                    new Car(rails.get(3), rails.get(4), null,
                                            Color.BLUE), Color.RED), Color.BLUE), 1);
        } catch (Exception e) {
            System.out.println("The rail is taken!");
        }
        locomotives.add(l);
    }

    public void initializeAForBlueStation() {
        Locomotive l = null;
        try {
            l = new Locomotive(rails.get(0), rails.get(6),
                    new Car(rails.get(6), rails.get(1),
                            new Car(rails.get(1), rails.get(2),
                                    new Car(rails.get(2), rails.get(3), null,
                                            Color.BLUE), Color.RED), Color.BLUE), 1);
        } catch (Exception e) {
            System.out.println("The rail is taken!");
        }
        locomotives.add(l);
    }

    public void initializeAForRedStation() {
        try {
            Locomotive l = new Locomotive(rails.get(3), rails.get(4),
                    new Car(rails.get(4), rails.get(7),
                            new Car(rails.get(7), rails.get(5),
                                    new Car(rails.get(5), rails.get(0), null,
                                            Color.BLUE), Color.RED), Color.BLUE), 1);
            locomotives.add(l);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("The rail is taken!");
        }
    }

    public void initializeB(int railID, int prevRailId) {
        try {
            Locomotive l = new Locomotive(rails.get(railID), rails.get(prevRailId), null, 2);
            locomotives.add(l);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("The rail is taken!");
        }
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


    //  ___________________________________________
    // |                                           |
    // |        File reading helper functions      |
    // |___________________________________________|
    //
    private void readRails(BufferedReader in) throws Exception {
        int numRails = Integer.parseInt(in.readLine());

        for (int i = 0; i < numRails; i++) {
            String[] line = in.readLine().split(" ");
            int from = Integer.parseInt(line[0]);
            int to = Integer.parseInt(line[1]);

            Rail r;

            // if a color is defined -> make it a Station
            if(line.length > 2){
                Color c = Color.values()[Integer.parseInt(line[2])];
                r = new Station(null, null, c);
            }
            else
                r = new Rail(null, null);

            // read from
            if (from != -1 && from < rails.size()) {
                r.setFrom(rails.get(from));
                // if not set yet, set an available end to this rail
                if (rails.get(from).getTo() == null)
                    rails.get(from).setTo(r);
            }

            // read to
            if (to != -1 && to < rails.size()){
                r.setTo(rails.get(to));
                // if not set yet, set an available end to this rail
                if(rails.get(to).getFrom() == null)
                    rails.get(to).setFrom(r);
            }

            rails.add(r);
        }
    }

    private void readSwitches(BufferedReader in) throws Exception {
        int numSwitches = Integer.parseInt(in.readLine());

        for (int i = 0; i < numSwitches; i++) {
            String[] line = in.readLine().split(" ");

            // create a new Switch and add it to the rails and switches
            Switch sw = new Switch(null, null, new ArrayList<>());
            rails.add(sw);
            switches.add(sw);

            // read from
            int from = Integer.parseInt(line[0]);
            if(from != -1) {
                sw.setFrom(rails.get(from));
                if (rails.get(from).to == null)
                    rails.get(from).setTo(sw);
                else
                    rails.get(from).setFrom(sw);
            }

            // read all the alternative rails
            for (int j = 1; j < line.length; j++) {
                int idx = Integer.parseInt(line[j]);

                if(idx == -1)
                    sw.alternativeWays.add(null);
                else {
                    sw.alternativeWays.add(rails.get(idx));
                    // if not set yet, set an available end to this switch
                    if (rails.get(idx).to == null)
                        rails.get(idx).setTo(sw);
                    else
                        rails.get(idx).setFrom(sw);
                }
            }
            // set to to the first alternative rail
            sw.setTo(sw.alternativeWays.get(0));
        }
    }

    private void readTunnelEndPoints(BufferedReader in) throws Exception {
        String[] line = in.readLine().split(" ");
        for (String aLine : line) {
            int idx = Integer.parseInt(aLine);
            tunnelEndPoints.add(rails.get(idx));
        }
    }

    private void readEntryPoints(BufferedReader in) throws Exception {
        String[] line = in.readLine().split(" ");
        for (String aLine : line) {
            int idx = Integer.parseInt(aLine);
            enterPoints.add(rails.get(idx));
        }
    }

    //
    //       End of file reading helper functions
    //  ______________________________________________
    //  ______________________________________________
}
