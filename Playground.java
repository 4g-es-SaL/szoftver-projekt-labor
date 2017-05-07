import java.io.*;
import java.util.ArrayList;

/**
 * Manages the objects({@link Rail}s, {@link Car}s) of the game.
 */
public class Playground {

    protected long startTime;
    protected Program program;
    protected ArrayList<Locomotive> locomotives = new ArrayList<>();
    protected ArrayList<Rail> rails = new ArrayList<>();
    protected ArrayList<Rail> enterPoints = new ArrayList<>();
    protected Tunnel tunnel;
    private ArrayList<Rail> tunnelEndPoints = new ArrayList<>();

    /**
     * Creat a new instance of Playground
     * @param f The file that stores the map of {@link Rail}s.
     *
     * A file tartalma a következő:
     *
         * R
         * from0 to0 x0 y0 [{sz0 | from02, to02}]
         * from1 to1 x1 y1 [{sz1 | from12, to12}]
         * ...
         * fromR-1 toR-1 xR-1 yR-1 [{szR-1 | fromR-12, toR-12}]
         * [ENTER]
         * S
         * from0 to0 x0 y0 alt00 alt01 ...
         * from1 to1 x1 y1 alt10 alt11 ...
         * ...
         * fromS-1 toS-1 xS-1 yS-1 altS-10 altS-11 ...
         * [ENTER]
         * T
         * E0
         * C0
         * locospeed0 locoprev0 lococurr0
         * color00 prev00 curr00
         * color01 prev01 curr01
         * …
         * color0C0-1 prev0C0-1 curr0C0-1
         * E1
         * C1
         * locospeed1 locoprev1 lococurr1
         * …
         * …
         * ET-1
         * CT-1
         * …
         * [ENTER]
         * {t0 t1 t2 ... | [SPACE]}
     *
     * ahol R = #Rails + #Stations
     * a következő R sorban:
     *      - fromi és toi mutatja, hogy az i-dik sínnek ki a fromja és toja,
     *        és ha az i-dik éppen Station, akkor a 3-dik számérték a színét
     *        jelenti.
     *      - Ha 4 számérték van az i-dik sínnek, akkor az egy CrossRail
     *        (kereszteződés). Ekkor 2 fromja és 2 to-ja van, az fromi-ból
     *        az toi-ba, illetve from2i-ból a to2i felé haladhat tovább a
     *        vonat. Egy CrossRail nem lehet station, tehát nem lehet színe.
     *
     * S = #Switches
     * következő S sorban:
     *      fromi, toi, alti0, alti1, ... mutatják az i-dik Switch
     *      megfelelő tulajdonságait
     *
     *
     * T = #Trains
     * következő T blokkban:
     *      Ei: hanyadik körben lép be
     *      Ci: kocsik száma
     *      locoprev lococurr: melyik sínről jött és melyiken van
     *      locospeed: a mozdony sebessége
     *      Következő Ci sor:
     *
     * Következő sor:
     * A lehetséges Tunnel építési pontok
     *
     */

    Playground(File f, Program p) {
        Rail.idGenerator = 0;
        program = p;
        try(FileInputStream in = new FileInputStream(f)) {
            byte[] rawData = new byte[(int) f.length()];
            in.read(rawData);
            in.close();

            String originalFileInput = new String(rawData, "UTF-8");
            String windowsCompatibleFileInput = originalFileInput.replace("\r\n", "\n");
            String[] data = windowsCompatibleFileInput.split("\n\n");

            createRails(data);
            createTrains(data);
            readTunnelEndPoints(data);

            // create and add the only Tunnel object to the rails
            tunnel = new Tunnel(null, null);
            rails.add(tunnel);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //region File reading helper functions

    private void createTrains(String[] data) throws Exception {
        String[] trainData = data[2].split("\n");
        int numTrains = Integer.parseInt(trainData[0]);
        int dataPointer = 1;

        for (int i = 0; i < numTrains; i++) {
            int entryTime = Integer.parseInt(trainData[dataPointer++]);
            int numCars = Integer.parseInt(trainData[dataPointer++]);

            String[] locomotiveData = trainData[dataPointer++].split(" ");
            int speed = Integer.parseInt(locomotiveData[0]);
            int prevRailID = Integer.parseInt(locomotiveData[1]);
            int currentRailID = Integer.parseInt(locomotiveData[2]);

            Locomotive locomotive = new Locomotive(rails.get(currentRailID), rails.get(prevRailID), null, speed, entryTime);
            program.addCar(locomotive);

            for (int j = 0; j < numCars; j++) {
                String[] carData = trainData[dataPointer++].split(" ");

                Color color = Color.values()[Integer.parseInt(carData[0])];
                prevRailID = Integer.parseInt(carData[1]);
                currentRailID = Integer.parseInt(carData[2]);
                Car newCar = new Car(rails.get(currentRailID), rails.get(prevRailID), null, color, entryTime);
                program.addCar(newCar);
                locomotive.getLastCar().next = newCar;
            }
            locomotives.add(locomotive);
        }

        for (Locomotive locomotive : locomotives) {
            System.out.println(locomotive.toString());
        }
    }

    /**
     *
     * @param data input file in blocks
     * @throws Exception
     *
     * first it creates the proper number of instances of
     * Rail, Station and CrossRail classes, then it calls
     * readRails() and readSwitches. These functions bind
     * all these togather
     */
    private void createRails(String[] data) throws Exception {
        String[] railData = data[0].split("\n");
        String[] switchData = data[1].split("\n");

        int numRails = Integer.parseInt(railData[0]);
        int numSwitches = Integer.parseInt(switchData[0]);

        for (int i = 0; i < numRails; i++) {
            String[] tmp = railData[i+1].split(" ");
            if(tmp.length == 5)
                rails.add(new Station(null, null, null));
            else if(tmp.length == 6)
                rails.add(new CrossRail(null, null, null, null));
            else
                rails.add(new Rail(null, null));
        }
        for (int i = 0; i < numSwitches; i++) {
            rails.add(new Switch(null, null, null));
        }

        readRails(numRails, railData);
        readSwitches(numRails, numSwitches, switchData);

        //----------------------------------------------
        // a little print. you can delete it
        for (Rail rail : rails) {
            System.out.println(rail.toString());
        }
    }

    /**
     *
     * @param numRails number of Rails
     * @param railData the Rail data block splitted along "\n" characters
     * @throws Exception
     *
     * It reads the rail block (1st block) of the input
     * and initializes the relationships
     */
    private void readRails(int numRails, String[] railData) throws Exception {

        for (int i = 0; i < numRails; i++) {
            String[] line = railData[i+1].split(" ");
            int fromID = Integer.parseInt(line[0]);
            int toID = Integer.parseInt(line[1]);
            int x = Integer.parseInt(line[2]);
            int y = Integer.parseInt(line[3]);
            Rail from = null, to = null;

            if(fromID > -1)
                from = rails.get(fromID);
            if(toID > -1)
                to = rails.get(toID);

            Rail current = rails.get(i);
            current.setFrom(from);
            current.setTo(to);
            program.addRail(current, x, y);

            // total ertelmetlen, de a doksiban ugy maradt,
            // hogy a rail blokkban vannak a Station-k és
            // a CrossRail-k is, ugyhogy ime...
            if(line.length == 5){
                Color c = Color.values()[Integer.parseInt(line[4])];
                Station tmp = (Station)current;
                tmp.color = c;
                program.addStation(tmp, x, y);
            }
            else if(line.length == 6){
                int from2ID = Integer.parseInt(line[4]);
                int to2ID = Integer.parseInt(line[5]);

                Rail from2 = null, to2 = null;

                if(from2ID > -1)
                    from2 = rails.get(from2ID);
                if(to2ID > -1)
                    to2 = rails.get(to2ID);

                CrossRail cross = (CrossRail)current;
                cross.setFrom2(from2);
                cross.setTo2(to2);
                program.extendRailToCrossRail(cross);
            }
        }
    }

    /**
     *
     * @param startID this is needed to be able to follow the row-continuity of the input file
     * @param numSwitches number of switches
     * @param switchData String array of lines, that contains the Switches descriptions.
     * @throws Exception
     *
     * It reads the Switch block (2nd block) of the input
     * and initializes the relationships
     */
    private void readSwitches(int startID, int numSwitches, String[] switchData) throws Exception {

        for (int i = 0; i < numSwitches; i++) {
            String[] line = switchData[i+1].split(" ");

            int fromID = Integer.parseInt(line[0]);
            int toID = Integer.parseInt(line[1]);
            int x = Integer.parseInt(line[2]);
            int y = Integer.parseInt(line[3]);
            Rail from = null, to = null;

            if(fromID > -1)
                from = rails.get(fromID);
            if(toID > -1)
                to = rails.get(toID);

            ArrayList<Rail> alternativeRails = new ArrayList<>();
            alternativeRails.add(to);
            for (int j = 4; j < line.length; j++) {
                Rail alt = null;
                int altID = Integer.parseInt(line[j]);
                if(altID > -1)
                    alt = rails.get(altID);
                alternativeRails.add(alt);
            }

            Switch sw = (Switch) rails.get(startID + i);
            sw.setFrom(from);
            sw.setTo(to);
            sw.alternativeWays = alternativeRails;
            program.addSwitch(sw, x, y);
        }
    }

    private void readTunnelEndPoints(String[] data) throws Exception {
        String[] tunnelEndData = data[3].split(" ");
        for (String tep : tunnelEndData) {
            int idx = Integer.parseInt(tep);
            //tunnelEndPoints.add(rails.get(idx));
            program.extendRailToMountainEntryPoint(rails.get(idx));
        }
    }

    private void readEntryPoints(BufferedReader in) throws Exception {
        String[] line = in.readLine().split(" ");
        for (String aLine : line) {
            int idx = Integer.parseInt(aLine);
            enterPoints.add(rails.get(idx));
        }
    }
    //endregion

    /**
     * Calls all locomotives {@link Locomotive#runTurn()}.
     * @return 1 if there was an error, 2 if victory, 0 otherwise.
     */
    public int runTurn() {
        Integer res = runLocomotives();
        if (res == 1){
            return 1;
        }
        if (areTrainsEmpty() && areStationsEmpty()) {
            return 2;
        }
        return 0;

    }

    //region runTurn() privates

    /**
     * If you can't figure out by the name, you are not worthy to be called 'programmer'.
     */
    protected int runLocomotives() {
        for (Locomotive loc:locomotives) {
            int res = loc.runTurn();
            if (res == 1) {
                return res;
            }
            System.out.println(loc);
            for (Car car : loc) {
                program.updateCar(car);
            }
        }
        return 0;
    }

    /**
     * If you can't figure out by the name, you are not worthy to be called 'programmer'.
     */
    protected boolean areTrainsEmpty() {
        for (Locomotive loc:locomotives) {
            if (!loc.isTrainEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * If you can't figure out by the name, you are not worthy to be called 'programmer'.
     */
    protected boolean areStationsEmpty() {
        for (Rail r : rails) {
            try {
                Station s = (Station) r;
                if (!s.isEmpty()) {
                    return false;
                }
            } catch(Exception e) {
                //Not station, pass
            }
        }
        return true;
    }

    //endregion

    /**
     * Changes the {@link Switch} direction.
     * @param id Identifies the Switch.
     */
    public void changeSwitch(int id) {
        ((Switch)rails.get(id)).changeDir();
    }


    /**
     * @param id Identifies the Rail which will be the Tunnel's end.
     */
    public Tunnel buildTunnelEnd(int id){
        return buildTunnelEnd(tunnelEndPoints.get(id));
    }

    public Tunnel buildTunnelEnd(Rail end) {
        tunnel.buildTunnel(end);
        System.out.println(this.tunnel);
        return tunnel;
    }
    
    /**
     * Builds new {@link Tunnel}.
     * @param e1 Id of one of the rails which will be the Tunnel's end.
     * @param e2 Id of one of the rails which will be the Tunnel's end.
     */
    public void buildNewTunnel(int e1, int e2) {
        tunnel.buildTunnel(this.rails.get(e2));
        tunnel.buildTunnel(this.rails.get(e1));
        System.out.println(this.tunnel);
    }

    /**
     * Destroys the {@link Tunnel}.
     */
    public void destroyTunnelEnd() {
        tunnel.destroyTunnel();
        System.out.println(this.tunnel);
    }

    /**
     * Adds passengers with given colour to given station, calls {@link Station#addPassanger} of given station.
     * @param id Rail ID of the {@link Station}, to which the passengers are to be added.
     * @param colorID {@link Color} of the passengers to be added.
     */
    public void addPassenger(int id, int colorID) {
        Station s = (Station) rails.get(id);
        Color c = Color.values()[colorID];
        s.addPassanger(c);
        System.out.println(s);
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

    /**
    * Prints the map, properties of all rails
    */
    public void printMap() {
        for (Rail r : rails) {
            System.out.println(r);
        }
    }
}
