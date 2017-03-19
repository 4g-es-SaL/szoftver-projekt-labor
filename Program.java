
import java.io.File;
import java.util.Scanner;

/**
 * Communicates with the user and the {@link Playground}.
 */
public class Program {

    protected Playground playground;

    /**
     * MAP:      (BLUE)
     *              0      sw|6|
     *          ________  ________
     *         |      /           |
     *     5   |    /             |   1
     *         |  /  (tunnel)     |
     *          /
     *         |                  |
     *  sw|7|  |                  |   2
     *         |_______  _________|
     *
     *             4         3 (RED)
     */


    /**
     * Starts the program.
     * @param args Ignored.
     */
    public static void main(String[] args) {
        // TODO implement here
        Program p = new Program();
        Scanner scanner = new Scanner(System.in);
        int userInput = -1;

        while (userInput != 0) {
            printInfo();
            userInput = scanner.nextInt();
            switch (userInput) {
                case 1:
                    p.initialization();
                    break;
                case 2:
                    p.ATrainMoves();
                    break;
                case 3:
                    p.BTrainMoves();
                    break;
                case 4:
                    p.switchTheSwitch();
                    break;
                case 5:
                    p.BThroughTheSwitch();
                    break;
                case 6:
                    p.buildSomeTunnels();
                    break;
                case 7:
                    p.BTroughTheTunnel();
                    break;
                case 8:
                    p.destroySomeTunnels();
                    break;
                case 9:
                	p.passRedStationWithFullCars();
                	break;
                case 10:
                	p.passBlueStationWithFullCars();
                	break;
                case 11:
                    p.collision();
                    break;
            }
        }
    }

	private static void printInfo() {
        System.out.println("A pályán két vonat található.\n" +
                "“A” vonat 1 mozdonyból és 3 kocsiból áll. A kocsik színe a vonat elejéről: kék, piros, kék. Sebessége 1.\n" +
                "“B” vonat 1 mozdonyból áll. Sebessége 2.\n" +
                "Válasszon jelenetet:\n" +
                "0 - Kilépés\n" +
                "1 - Inicializáció\n" +
                "2 - “A” előre halad.\n" +
                "3 - “B” előre halad.\n" +
                "4 - Váltó átállítása.\n" +
                "5 - “B” vonat áthaladása váltón.\n" +
                "6 - Alagút építés.\n" +
                "7 - “B” vonat áthaladása alagúton.\n" +
                "8 - Alagút lebontása.\n" +
                "9 - “A” áthalad a piros színű állomáson, mikor még egy kocsija sem ürült.\n" +
                "10 - “A” áthalad a kék színű állomáson, mikor még egy kocsija sem ürült.\n" +
                "11 - Ütközés\n");
    }

    private void initialization() {
        Rail.idGenerator = 0;
        File f = new File("map.txt");
        playground = new Playground(f);
        MethodPrinter.reset();
    }

    private void ATrainMoves() {
        MethodPrinter.disablePrint();
        initialization();
        playground.initializeA();
        MethodPrinter.enablePrint();
        playground.runTurn();
        MethodPrinter.reset();
    }

    private void BTrainMoves() {
        MethodPrinter.disablePrint();
        initialization();
        playground.initializeB(2, 1);
        MethodPrinter.enablePrint();
        playground.runTurn();
        MethodPrinter.reset();
    }

    private void switchTheSwitch(){
        MethodPrinter.disablePrint();
        initialization();
        MethodPrinter.enablePrint();
        playground.changeSwitch(0);
        MethodPrinter.reset();
    }

    private void BThroughTheSwitch(){
        MethodPrinter.disablePrint();
        initialization();
        playground.initializeB(4, 3);
        MethodPrinter.enablePrint();
        playground.runTurn();
        MethodPrinter.reset();
    }

    private void buildSomeTunnels(){
        MethodPrinter.disablePrint();
        initialization();
        MethodPrinter.enablePrint();
        playground.buildTunnelEnd(0);
        playground.buildTunnelEnd(1);
        MethodPrinter.reset();
    }

    private void BTroughTheTunnel(){
        MethodPrinter.disablePrint();
        initialization();
        playground.initializeB(7, 4);
        playground.buildTunnelEnd(0);
        playground.buildTunnelEnd(1);
        playground.changeSwitch(1);
        MethodPrinter.enablePrint();
        playground.runTurn();
        MethodPrinter.reset();
    }

    private void destroySomeTunnels(){
        MethodPrinter.disablePrint();
        initialization();
        playground.buildTunnelEnd(0);
        playground.buildTunnelEnd(1);
        MethodPrinter.enablePrint();
        playground.destroyTunnelEnd(0);
        MethodPrinter.reset();
    }
  
    private void passBlueStationWithFullCars(){
    	MethodPrinter.disablePrint();
    	initialization();
    	playground.initializeA();
    	MethodPrinter.enablePrint();
//    	System.out.println("pisi-kaka"); // WHAT THE FUCK IS THIS SHIT?!4?!4
    	playground.runTurn();
        playground.runTurn();
        playground.runTurn();
        playground.runTurn();
        playground.runTurn();
        MethodPrinter.reset();
    }

    private void collision(){
        MethodPrinter.disablePrint();
        initialization();
        playground.initializeB(0, 5);
        playground.initializeA();
        MethodPrinter.enablePrint();

        int isCollision = 0;
        while (true){
            isCollision = playground.runTurn();

            if( isCollision == 1 ){
                System.out.println("\n<<Collision Exception!>>\n");
                break;
            }
        }
        MethodPrinter.reset();
    }
    
    private void passRedStationWithFullCars(){
    	MethodPrinter.disablePrint();
    	initialization();
    	playground.initializeAForRedStation();
    	MethodPrinter.enablePrint();
    	playground.runTurn();
        playground.runTurn();
        playground.runTurn();
        playground.runTurn();
        MethodPrinter.reset();
    }
    
}