
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
     * MAP:
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
                "9 - “A” áthalad egy piros színű állomáson miután már egyszer áthaladt kéken.\n" +
                "10 - “A” áthalad a kék színű állomáson, mikor még egy kocsija sem ürült.\n" +
                "11 - Ütközés\n");
    }

    private void initialization() {
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
}