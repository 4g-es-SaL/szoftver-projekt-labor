
import java.io.File;
import java.util.Scanner;

/**
 * Created by matech on 2017. 02. 20..
 */
public class Program {

    /**
     *
     */
    protected Playground playground;

    /**
     * MAP:      (BLUE)
     *              0      sw|6|
     *          ________  ________
     *         |      /           |
     *     5   |    /             |   1 (RED)
     *         |  /  (tunnel)     |
     *          /
     *         |                  |
     *  sw|7|  |                  |   2
     *         |_______  _________|
     *
     *             4         3
     */


    /**
     * @param args
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
                case 11:
                    p.collision();
                    break;
            }
        }
    }

	private static void printInfo() {
        System.out.println("A p�ly�n k�t vonat tal�lhat�.\n" +
                "�A� vonat 1 mozdonyb�l �s 3 kocsib�l �ll. A kocsik sz�ne a vonat elej�r�l: k�k, piros, k�k. Sebess�ge 1.\n" +
                "�B� vonat 1 mozdonyb�l �ll. Sebess�ge 2.\n" +
                "V�lasszon jelenetet:\n" +
                "0 - Kil�p�s\n" +
                "1 - Inicializ�ci�\n" +
                "2 - �A� el�re halad.\n" +
                "3 - �B� el�re halad.\n" +
                "4 - V�lt� �t�ll�t�sa.\n" +
                "5 - �B� vonat �thalad�sa v�lt�n.\n" +
                "6 - Alag�t �p�t�s.\n" +
                "7 - �B� vonat �thalad�sa alag�ton.\n" +
                "8 - Alag�t lebont�sa.\n" +
                "9 - �A� �thalad a piros sz�n� �llom�son, mikor m�g egy kocsija sem �r�lt.\n" +
                "10 - �A� �thalad a k�k sz�n� �llom�son, mikor m�g egy kocsija sem �r�lt.\n" +
                "11 - �tk�z�s\n");
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
        playground.initializeB();
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
        playground.initializeB();
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
        playground.initializeB();
        playground.buildTunnelEnd(0);
        playground.buildTunnelEnd(1);
        playground.changeSwitch(1);
        MethodPrinter.enablePrint();
        playground.runTurn();
        playground.runTurn();
        playground.runTurn();
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
    	System.out.println("pisi-kaka");
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
        playground.initializeA();
        playground.initializeB();
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